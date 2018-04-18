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
import kotlin.test.assertTrue


internal class MySQLConnectionTest {


    private var testConnection: Connection? = MySQLConnection().getConnection()


    @Test
    fun testConnection() {
        val statement: Statement = testConnection!!.createStatement()
        MySQLConnection().closeStatement(statement)
        assertTrue(statement.isClosed())
    }

    @After
    fun after() {
        MySQLConnection().closeConnection(testConnection)
    }

}