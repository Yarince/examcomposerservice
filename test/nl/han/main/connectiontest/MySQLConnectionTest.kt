package nl.han.main.databaseconnection

import org.junit.jupiter.api.Test
import java.sql.Connection
import kotlin.test.assertNotNull
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.junit.After
import org.junit.Before
import ch.qos.logback.core.db.DBHelper.closeStatement
import com.mysql.cj.api.MysqlConnection
import java.sql.PreparedStatement
import java.sql.Statement
import kotlin.test.assertFalse
import kotlin.test.assertNull
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