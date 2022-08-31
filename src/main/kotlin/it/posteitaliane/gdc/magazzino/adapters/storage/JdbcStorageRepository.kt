package it.posteitaliane.gdc.magazzino.adapters.storage

import it.posteitaliane.gdc.magazzino.core.*
import it.posteitaliane.gdc.magazzino.core.ports.OperatorRepository
import it.posteitaliane.gdc.magazzino.core.ports.StorageRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Date
import java.sql.Timestamp
import java.sql.Types
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Repository
class JdbcStorageRepository(private val template:JdbcTemplate) : StorageRepository, OperatorRepository {

    private companion object {
        const val SN_QUERY:String = "SELECT COUNT(*) FROM vista_giacenze WHERE Seriale = ? LIMIT 1"
        const val PT_QUERY:String = "SELECT COUNT(*) FROM vista_giacenze WHERE PtNumber = ? LIMIT 1"
        const val POSITIONS_QUERY:String = "SELECT Posizione FROM posizioni WHERE NomeDc = ?"
        const val ORDER_QUERY = "SELECT " +
                "id,user,opdate,operation,docid,partner,numdoc,issuedate," +
                "oda,remarks,receipient,filepath,dc,dccode,op,docsubject" +
                " FROM view_orders" +
                " LIMIT ?,?"
    }

    private val Rollback = MagazzinoApi(template)
        .build("rollbackTransaction").config()

    private val Commit = MagazzinoApi(template)
        .build("commitTransaction")
        .config {
            inParam("paramNomeDC")
        }

    private val InternalLoad = MagazzinoApi(template)
        .build("inserimentoRicevute")
        .config {
            inParam("paramNomeDatacenter")
            inParam("paramDestinatarioRic")
            inParam("paramTipoRicevute")

            inParam("paramFileDoc")
            inParam("paramNoteDoc")

            outParam("codiceDocumento")
        }

    private val Load = MagazzinoApi(template)
        .build("carico")
        .config{
            inParam("paramNumDoc")
            inParam("paramNomeDc")
            inParam("paramPosizioneDc")
            inParam("paramMerce")
            inParam("paramQty", Types.INTEGER)

            inParam("paramDestFinale")
            inParam("paramSerialeDisp")
            inParam("paramOdaMerce")
            inParam("paramNoteMerce")
            inParam("paramTitolare")
            inParam("paramCodiceCollo")
            inParam("paramPtnumberDisp")
        }

    override fun findOrders(filter: (Order.() -> Boolean)?, offset:Int, limit:Int): List<Order> {
        val list = template.queryForList(ORDER_QUERY, offset, limit)
            .map {
                val op = findByUid(it["user"] as String)

                val order = MutableOrder(op, it["opdate"] as LocalDateTime,
                    it["dc"] as String,
                    when(it["op"]) { "CARICO" -> OrderType.LOAD else -> OrderType.UNLOAD },
                    when(it["DOCSUBJECT"]) { "INTERNAL" -> OrderSubject.INTERNAL else -> OrderSubject.PARTNER })

                order.apply {
                    partner = it["partner"] as String?

                    if( it["numdoc"] != null ) {
                        document = Document(it["numdoc"] as String, (it["issuedate"] as Date).toLocalDate(),
                            partner ?: "POSTE ITALIANE S.P.A.")
                    }

                    oda = it["oda"] as String?
                    remarks = it["remarks"] as String?
                    rep = it["receipient"] as String?

                    filepath = it["filepath"] as String?
                }

                order
            }

        if(filter == null)
            return list
        else
            return list.filter(filter)
    }

    override fun positionsAt(location: String): List<String> {
        return template.queryForList(POSITIONS_QUERY, String::class.java)
    }

    override fun ptExists(s: String?): Boolean {
        val count:Int = template.queryForObject(PT_QUERY, Int::class.java, s)

        return count != 0
    }

    override fun registerLoad(order: Order, item: String, position: String, amount: Int) {
        val(stato, mess) = Load(order.uid, order.id, order.location, position, item, amount,
            null, null, null, null, null, null, null)
    }

    override fun registerOrder(order: Order) {

        val o = order as MutableOrder

        var Call = InternalLoad

        if( o.type == OrderType.LOAD && o.subject == OrderSubject.INTERNAL ) {

            Call = InternalLoad;

        }

        val(stato, mess, docid) = Call(uid=o.uid, o.location, o.rep, "FITTIZIA", null, o.remarks)

        if(stato!=0) {
            Rollback(uid = o.uid)
            throw Exception("CREATE RECEIT FAIL: $mess")
        }

        o.id = docid

        run loop@ {
            o.lines.forEach {
                var (stato2, mess2) = Load(
                    uid = it.order.uid, it.order.id, it.order.location, it.position, it.item, it.amount,
                    null, it.sn, null, null, null, null, it.pt
                )

                if (stato2 != 0) {
                    Rollback(uid = o.uid)
                    throw Exception("LOAD ORDER LINE FAIL: $mess2")
                }
            }
        }

        Commit(uid = order.uid, order.location)
    }

    override fun snExists(sn: String?): Boolean {
        val count:Int = template.queryForObject(SN_QUERY, Int::class.java, sn)

        return count != 0
    }

    override fun findByUid(uid: String): Operator {
        return Operator(uid,Area("", listOf()), Permission.READ)
    }
}