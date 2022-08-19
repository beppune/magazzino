package it.posteitaliane.gdc.magazzino.adapters.storage

import it.posteitaliane.gdc.magazzino.core.Order
import it.posteitaliane.gdc.magazzino.core.OrderLine
import it.posteitaliane.gdc.magazzino.core.ports.StorageRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class JdbcStorageRepository(val template:JdbcTemplate) : StorageRepository {

    private companion object {
        val SN_QUERY:String = "SELECT COUNT(*) FROM vista_giacenze WHERE Seriale = ? LIMIT 1"
        val PT_QUERY:String = "SELECT COUNT(*) FROM vista_giacenze WHERE PtNumber = ? LIMIT 1"
    }

    override fun cancelOrder(order: Order) {
        TODO("Not yet implemented")
    }

    override fun positionsAt(location: String): List<String> {
        TODO("Not yet implemented")
    }

    override fun ptExists(pt: String?): Boolean {
        val count:Int = template.queryForObject(PT_QUERY, Int::class.java, pt)

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