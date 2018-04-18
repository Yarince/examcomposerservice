package nl.han.main.databaseconnection


import java.io.FileReader
import java.sql.*
import java.util.Properties
import java.sql.DriverManager


class MySQLConnection {
    /*
    In case that the database + the queries are ready to use this class has to be extended.
    It needs 2 more variables and an extension of the testConnection method to retrieve data
    by prepared statements.
    */
    private var conn: Connection? = null

    fun testConnection(): Connection? {

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
        } finally {
            conn?.close()
        }
        return null

    }
}
