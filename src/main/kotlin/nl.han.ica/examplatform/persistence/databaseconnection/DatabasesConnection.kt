package nl.han.ica.examplatform.persistence.databaseconnection

import java.io.FileReader
import java.sql.*
import java.util.Properties
import java.sql.DriverManager
import java.sql.SQLException



object MySQLConnection {

    private val databaseProperties : Properties
    /*
    The value databseProperties is initialized in the init{} method
     */
    init {
        databaseProperties = initializeProperties()
    }

    /*
    In case that the database + the queries are ready to use this class has to be extended.
    It needs 2 more variables(preparedStatement+resultSet) and an extension of the getConnection
    method to retrieve data by prepared statements.
    */
    private var conn: Connection? = null


    private fun establishDatabaseConnection() {
        try {
            conn = connectDatabase(getDatabaseConnectionUrl(databaseProperties), getDatabaseUsername(databaseProperties), getDatabasePassword(databaseProperties), getDrivers(databaseProperties))
        } catch (e: SQLException){
            e.printStackTrace()
        }
    }

    fun getConnection() : Connection? {
        establishDatabaseConnection()
        return conn
    }

    private fun initializeProperties(): Properties {
        val databaseProperties = Properties()
        val reader = FileReader(System.getProperty("user.dir") + "\\src\\main\\kotlin\\nl.han.ica\\examplatform\\config\\databaseconfig\\application.properties")
        databaseProperties.load(reader)
        return databaseProperties
    }

    private fun connectDatabase(connectionURL: String, username: String, password: String, drivers: String): Connection {
        Class.forName(drivers)
        return DriverManager.getConnection(connectionURL, username, password)
    }

    fun closeConnection(con: Connection?) {
        try {
            con?.close()
            conn = null
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun closeStatement(stmt : Statement) {
        try {
            stmt.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun getDatabaseConnectionUrl(properties: Properties): String {
        return properties.getProperty("jdbc.url")
    }

    private fun getDatabaseUsername(properties: Properties): String {
        return properties.getProperty("jdbc.username")
    }

    private fun getDatabasePassword(properties: Properties): String {
        return properties.getProperty("jdbc.password")
    }

    private fun getDrivers(properties: Properties): String {
        return properties.getProperty("jdbc.driverClassName")
    }

}
