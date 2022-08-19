package it.posteitaliane.gdc.magazzino.adapters.storage

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.jdbc.core.JdbcTemplate

class TestJdbcStorageRepository {

    private val template:JdbcTemplate = mockk()
    private val repo:JdbcStorageRepository = JdbcStorageRepository(template)

    @BeforeEach
    fun setUp() {
        clearMocks(template)
    }

    @Test
    fun `should find specified sn`() {
        every { template.queryForObject(eq(JdbcStorageRepository.SN_QUERY), Int::class.java, any()) } returns 1

        var res = repo.snExists("SNEXISTS")

        verify { template.queryForObject(any(), Int::class.java, any()) }

        assertThat(res).isTrue

    }

    @Test
    fun `should not find specified pt`() {
        every { template.queryForObject(eq(JdbcStorageRepository.SN_QUERY), Int::class.java, any()) } returns 0

        var res = repo.snExists("SNNOTEXISTS")

        assertThat(res).isFalse

    }

    @Test
    fun `should not find specified pt if its null`() {
        every { template.queryForObject(eq(JdbcStorageRepository.SN_QUERY), Int::class.java, null) } returns 0

        var res = repo.snExists(null)

        assertThat(res).isFalse

    }

    @Test
    fun `should find specified pt`() {
        every { template.queryForObject(eq(JdbcStorageRepository.PT_QUERY), Int::class.java, any()) } returns 1

        var res = repo.ptExists("SNEXISTS")

        verify { template.queryForObject(any(), Int::class.java, any()) }

        assertThat(res).isTrue

    }

    @Test
    fun `should not find specified sn`() {
        every { template.queryForObject(eq(JdbcStorageRepository.PT_QUERY), Int::class.java, any()) } returns 0

        var res = repo.ptExists("SNNOTEXISTS")

        assertThat(res).isFalse

    }

    @Test
    fun `should not find specified sn if its null`() {
        every { template.queryForObject(eq(JdbcStorageRepository.PT_QUERY), Int::class.java, null) } returns 0

        var res = repo.ptExists(null)

        assertThat(res).isFalse

    }
}