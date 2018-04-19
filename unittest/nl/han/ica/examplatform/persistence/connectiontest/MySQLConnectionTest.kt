package nl.han.ica.examplatform.persistence.connectiontest

import org.junit.jupiter.api.Test
import java.sql.Connection
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.junit.After
import java.sql.Statement
import kotlin.test.assertTrue


internal class MySQLConnectionTest {

    private var databaseConnection: MySQLConnection? = MySQLConnection
    private var testConnection: Connection? = null

    @Test
    fun testConnectionByOpeningAndClosingAStatement() {
        testConnection = databaseConnection?.getConnection()
        testConnection?.let {
            val statement: Statement = it.createStatement()
            databaseConnection?.closeStatement(statement)
            assertTrue(statement.isClosed)
        }
    }

    @After
    fun after() {
        databaseConnection?.closeConnection(testConnection)
    }
}