package it.posteitaliane.gdc.magazzino.adapters.storage

import io.mockk.every
import io.mockk.mockk
import it.posteitaliane.gdc.magazzino.core.*
import it.posteitaliane.gdc.magazzino.core.ports.StorageRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("it")
class TestJdbcStorageRepositoryInternalLoad {

    @Autowired
    lateinit var repo:StorageRepository

    @Test
    fun `should register order`() {

        var order = mockk<MutableOrder>()
        every { order.uid } returns "MANZOGI9"
        every { order.type } returns OrderType.LOAD
        every { order.subject } returns OrderSubject.INTERNAL
        every { order.rep } returns "MANZO GIUSEPPE"
        every { order.location } returns repo.findLocations().find { it.code == "TOR" } as Location
        every { order.remarks } returns null
        every { order.lines } returns mutableListOf( OrderLine(order, "MERCE 1", "A-P01", 10) )
        every { order::id.set(any()) } answers { callOriginal() }
        every { order::id.get() } answers { callOriginal() }

        repo.registerOrder(order)

        order.lines.forEach {
            repo.registerLoad(order, it.item, it.position, it.amount)
        }

        assertThat(order.id).isNotNull().isNotBlank().isNotEmpty()


    }

    @Test
    @Disabled
    fun `should throw`() {

        var order = mockk<MutableOrder>()
        every { order.uid } returns "MANZOGI9"
        every { order.type } returns OrderType.LOAD
        every { order.subject } returns OrderSubject.INTERNAL
        every { order.rep } returns "MANZO GIUSEPPE"
        every { order.location } returns repo.findLocations().find { it.code == "TOR" } as Location
        every { order.remarks } returns null
        every { order.lines } returns mutableListOf(OrderLine(order, "MERCE 1", "A-P01", 10))
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
    @Disabled
    fun `should throw 2`() {

        var order = mockk<MutableOrder>()
        every { order.uid } returns "MANZOGI9"
        every { order.type } returns OrderType.LOAD
        every { order.subject } returns OrderSubject.INTERNAL
        every { order.rep } returns "MANZO GIUSEPPE"
        every { order.location } returns repo.findLocations().find { it.code == "TOR" } as Location
        every { order.remarks } returns null
        every { order.lines } returns mutableListOf(OrderLine(order, "MERCE 1", "A-P01", 10))
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

        val list = repo.findOrders()

        list.forEach(::println)

        assertThat(list).isNotEmpty
    }

    @Test
    fun `should retreive location list`() {

        val list = repo.findLocations()

        list.forEach(::println)

        assertThat(list).isNotEmpty
    }

}