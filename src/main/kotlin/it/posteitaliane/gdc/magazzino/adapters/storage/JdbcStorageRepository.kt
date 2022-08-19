package it.posteitaliane.gdc.magazzino.adapters.storage

import it.posteitaliane.gdc.magazzino.core.Order
import it.posteitaliane.gdc.magazzino.core.OrderLine
import it.posteitaliane.gdc.magazzino.core.ports.StorageRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class JdbcStorageRepository(private val template:JdbcTemplate) : StorageRepository {

    private companion object {
        const val SN_QUERY:String = "SELECT COUNT(*) FROM vista_giacenze WHERE Seriale = ? LIMIT 1"
        const val PT_QUERY:String = "SELECT COUNT(*) FROM vista_giacenze WHERE PtNumber = ? LIMIT 1"
        const val POSITIONS_QUERY = "SELECT Posizione FROM posizioni WHERE NomeDc = ?"
    }

    override fun cancelOrder(order: Order) {
        TODO("Not yet implemented")
    }

    override fun positionsAt(location: String): List<String> {
        return template.queryForList(POSITIONS_QUERY, String::class.java)
    }

    override fun ptExists(s: String?): Boolean {
        val count:Int = template.queryForObject(PT_QUERY, Int::class.java, s)

        return count != 0
    }

    override fun registerLoad(order: Order, item: String, position: String, amount: Int): OrderLine {
        TODO("Not yet implemented")
    }

    override fun registerOrder(order: Order) {
        TODO("Not yet implemented")
    }

    override fun snExists(sn: String?): Boolean {
        val count:Int = template.queryForObject(SN_QUERY, Int::class.java, sn)

        return count != 0
    }
}