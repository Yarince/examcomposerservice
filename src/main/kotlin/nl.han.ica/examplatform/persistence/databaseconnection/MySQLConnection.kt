package nl.han.ica.examplatform.persistence.databaseconnection

import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import java.util.*

/**
 * A singleton object that handles the connection with the MySQL database
 */
object MySQLConnection {

    /**
     * Holds the database properties, such as the connection URL, username and password.
     */
    private val databaseProperties: Properties

    init {
        databaseProperties = initializePropertiesOutsideJar()
    }

    /**
     * Retrieves a connection and returns it
     *
     * @return a database [Connection] on which queries can be executed
     */
    fun getConnection(): Connection? {
        return try {
            connectDatabase(getDatabaseConnectionUrl(databaseProperties), getDatabaseUsername(databaseProperties), getDatabasePassword(databaseProperties), getDrivers(databaseProperties))
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Reads the database properties, loads them into a [Properties] object and returns them
     *
     * @return loaded database properties
     */
    private fun initializeProperties(): Properties {
        val databaseProperties = Properties()
        val reader = FileReader(System.getProperty("user.dir") + "/src/main/resources/application.properties")
        databaseProperties.load(reader)
        return databaseProperties

    return databaseProperties
    }

    /**
     * Reads the database properties, loads them into a [Properties] object and returns them
     *
     * @return loaded database properties
     */
    private fun initializePropertiesOutsideJar() : Properties {
        val databaseProperties = Properties()

        try {
            val jarPath = File(this::class.java!!.protectionDomain.codeSource.location.path)
            val propertiesPath = jarPath.parentFile.absolutePath
            databaseProperties.load(FileInputStream("$propertiesPath/application.properties"))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return databaseProperties
    }

    /**
     * Handles the database connection using the MySQL driver
     *
     * @param connectionURL the URL of the DB that should be connected with
     * @param username the username that will be used to connect
     * @param password the corresponding password of the user
     * @param drivers the database drivers that are being used to connect
     * @return a MySQL database [Connection]
     */
    private fun connectDatabase(connectionURL: String, username: String, password: String, drivers: String): Connection {
        Class.forName(drivers)
        return DriverManager.getConnection(connectionURL, username, password)
    }

    /**
     * Closes a database connection
     *
     * @param connectionToClose [Connection] that should be closed
     */
    fun closeConnection(connectionToClose: Connection?) {
        try {
            connectionToClose?.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    /**
     * Closes a prepared statement
     *
     * @param stmt [Statement] that should be closed
     */
    fun closeStatement(stmt: Statement?) {
        try {
            stmt?.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun getDatabaseConnectionUrl(properties: Properties): String {
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
