package nl.han.ica.examplatform.persistence.connectiontest

import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.junit.After
import org.junit.Test
import org.mockito.Mock
import java.sql.Statement
import kotlin.test.assertTrue

internal class MySQLConnectionTest {

    private var databaseConnection = MySQLConnection

    @Mock
    private var testConnection = databaseConnection.getConnection()

    @Test
    fun testConnectionByOpeningAndClosingAStatement() {
        testConnection?.let {
            val statement: Statement = it.createStatement()
            databaseConnection.closeStatement(statement)
            assertTrue(statement.isClosed)
        }
    }

    @After
    fun after() {
        databaseConnection.closeConnection(testConnection)
    }
}