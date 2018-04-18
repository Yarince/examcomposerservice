package nl.han.main.databaseconnection

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import org

internal class MySQLConnectionTest {

    @Mock
    private
    lateinit var connection: Connection

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun testConnection() {
    }

    @AfterEach
    fun tearDown() {
    }
}