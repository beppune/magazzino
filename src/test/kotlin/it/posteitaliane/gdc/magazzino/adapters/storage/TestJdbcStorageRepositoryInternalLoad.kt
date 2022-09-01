package it.posteitaliane.gdc.magazzino.adapters.storage

import io.mockk.every
import io.mockk.mockk
import it.posteitaliane.gdc.magazzino.core.MutableOrder
import it.posteitaliane.gdc.magazzino.core.OrderLine
import it.posteitaliane.gdc.magazzino.core.OrderSubject
import it.posteitaliane.gdc.magazzino.core.OrderType
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("it")
class TestJdbcStorageRepositoryInternalLoad {

    @Autowired
    lateinit var template:JdbcTemplate

    @Test
    fun `should register order`() {

        val repo = JdbcStorageRepository(template)

        var order = mockk<MutableOrder>()
        every { order.uid } returns "MANZOGI9"
        every { order.type } returns OrderType.LOAD
        every { order.subject } returns OrderSubject.INTERNAL
        every { order.rep } returns "MANZO GIUSEPPE"
        every { order.location } returns "DC TORINO"
        every { order.remarks } returns null
        every { order.lines } returns mutableListOf( OrderLine(order, "MERCE 1", "A-P10", 10) )
        every { order::id.set(any()) } answers { callOriginal() }
        every { order::id.get() } answers { callOriginal() }

        repo.registerOrder(order)

        order.lines.forEach {
            repo.registerLoad(order, it.item, it.position, it.amount)
        }

        assertThat(order.id).isNotNull().isNotBlank().isNotEmpty()


    }

    @Test
    fun `should throw`() {

        val repo = JdbcStorageRepository(template)

        var order = mockk<MutableOrder>()
        every { order.uid } returns "MANZOGI9"
        every { order.type } returns OrderType.LOAD
        every { order.subject } returns OrderSubject.INTERNAL
        every { order.rep } returns "MANZO GIUSEPPE"
        every { order.location } returns "INVALID"
        every { order.remarks } returns null
        every { order.lines } returns mutableListOf(OrderLine(order, "MERCE 1", "A-P10", 10))
        every { order::id.set(any()) } answers { callOriginal() }
        every { order::id.get() } answers { callOriginal() }

        assertThatThrownBy {
            repo.registerOrder(order)

            order.lines.forEach {
                repo.registerLoad(order, it.item, it.position, it.amount)

            }
        }.hasMessageContaining("CREATE RECEIT FAIL")

    }
    @Test
    fun `should throw 2`() {

        val repo = JdbcStorageRepository(template)

        var order = mockk<MutableOrder>()
        every { order.uid } returns "MANZOGI9"
        every { order.type } returns OrderType.LOAD
        every { order.subject } returns OrderSubject.INTERNAL
        every { order.rep } returns "MANZO GIUSEPPE"
        every { order.location } returns "DC TORINO"
        every { order.remarks } returns null
        every { order.lines } returns mutableListOf(OrderLine(order, "MERCE 1", "AP10", 10))
        every { order::id.set(any()) } answers { callOriginal() }
        every { order::id.get() } answers { callOriginal() }

        assertThatThrownBy {
            repo.registerOrder(order)

            order.lines.forEach {
                repo.registerLoad(order, it.item, it.position, it.amount)

            }
        }.hasMessageContaining("LOAD ORDER LINE FAIL")

    }

    @Test
    fun `should retreive order list`() {
        val repo = JdbcStorageRepository(template)

        val list = repo.findOrders()

        list.forEach(::println)

        assertThat(list).isNotEmpty
    }

    @Test
    fun `should retreive location list`() {
        val repo = JdbcStorageRepository(template)

        val list = repo.findLocations()

        list.forEach(::println)

        assertThat(list).isNotEmpty
    }

}