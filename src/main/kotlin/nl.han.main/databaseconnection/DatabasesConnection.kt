package nl.han.ica.examplatform.persistence.databaseconnection


import java.io.FileReader
import java.sql.*
import java.util.Properties
import java.sql.DriverManager
import java.sql.SQLException




class MySQLConnection {
    /*
    In case that the database + the queries are ready to use this class has to be extended.
    It needs 2 more variables and an extension of the getConnection method to retrieve data
    by prepared statements.
    */
    private var conn: Connection? = null

    fun getConnection(): Connection? {

        val databaseProperties = Properties()
        val propertiesFile = System.getProperty("user.dir") + "\\src\\main\\resources\\application.properties"
        val reader = FileReader(propertiesFile)
        databaseProperties.load(reader)

        val drivers = databaseProperties.getProperty("jdbc.driverClassName")
        val connectionURL = databaseProperties.getProperty("jdbc.url")
        val username = databaseProperties.getProperty("jdbc.username")
        val password = databaseProperties.getProperty("jdbc.password")
        try {
            Class.forName(drivers)
            conn = DriverManager.getConnection(connectionURL, username, password)
            return conn
        } catch (e: SQLException){
            e.printStackTrace()
        }
        return null
    }

    fun closeConnection(con: Connection?) {
        try {
            if (null != conn) {
                con?.close()
                conn = null
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun closeStatement(stmt : Statement) {
        try {
            if (null != stmt) {
                stmt.close()
            }
        } catch (e: SQLException) {
            e.printStackTrace()

        }

    }

}
