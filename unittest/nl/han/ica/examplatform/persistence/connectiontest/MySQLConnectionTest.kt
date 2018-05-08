package nl.han.ica.examplatform.persistence.connectiontest

import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.junit.After
import org.junit.Test
import org.mockito.Mock
import java.sql.PreparedStatement
import kotlin.test.assertTrue

internal class MySQLConnectionTest {

    private var databaseConnection = MySQLConnection

    @Mock
    private var testConnection = databaseConnection.getConnection()

    @Test
    fun testConnectionByOpeningAndClosingAStatement() {
        testConnection?.let {
            val statement: PreparedStatement = it.prepareStatement("SELECT 1 FROM TESTTABLE")
            databaseConnection.closeStatement(statement)
            assertTrue(statement.isClosed)
        }
    }

    @After
    fun after() {
        databaseConnection.closeConnection(testConnection)
    }
}