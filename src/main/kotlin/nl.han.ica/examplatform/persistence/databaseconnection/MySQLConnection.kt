package nl.han.ica.examplatform.persistence.databaseconnection

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.responseexceptions.DatabaseException
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
 * A singleton object that handles the connection with the MySQL database.
 */
object MySQLConnection {
    private val logger = loggerFor(javaClass)

    /**
     * Holds the database properties, such as the connection URL, username and password.
     */
    private val databaseProperties: Properties = initializePropertiesOutsideJar()

    /**
     * Retrieves a connection and returns it.
     *
     * @return a database [Connection] on which queries can be executed
     */
    fun getConnection(): Connection? =
        try {
            connectDatabase(getDatabaseConnectionUrl(
                databaseProperties),
                getDatabaseUsername(databaseProperties),
                getDatabasePassword(databaseProperties),
                getDrivers(databaseProperties))
        } catch (e: SQLException) {
            logger.error("Error when getting connection", e)
            null
        }

    /**
     * Reads the database properties, loads them into a [Properties] object and returns them.
     *
     * @return loaded database properties
     */
    private fun initializeProperties(): Properties {
        val databaseProperties = Properties()
        val reader = FileReader(System.getProperty("user.dir") +
            "/src/main/resources/application.properties")
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

        return try {
            val jarPath = File(this::class.java!!.protectionDomain.codeSource.location.path)
            val propertiesPath = jarPath.parentFile.absolutePath
            databaseProperties.load(FileInputStream("$propertiesPath/application.properties"))

            databaseProperties
        } catch (e: IOException) {
            logger.error("Error when getting connection", e)
            throw DatabaseException("")
        }
    }

    /**
     * Handles the database connection using the MySQL driver.
     *
     * @param connectionURL [String] The URL of the DB that should be connected with
     * @param username [String] The username that will be used to connect
     * @param password [String] The corresponding password of the user
     * @param drivers [String] The database drivers that are being used to connect
     * @return [Connection] A MySQL database
     */
    private fun connectDatabase(
        connectionURL: String,
        username: String,
        password: String,
        drivers: String
    ): Connection {
        Class.forName(drivers)
        return DriverManager.getConnection(connectionURL, username, password)
    }

    /**
     * Closes a database connection.
     *
     * @param connectionToClose [Connection] that should be closed
     */
    fun closeConnection(connectionToClose: Connection?) {
        try {
            connectionToClose?.close()
        } catch (e: SQLException) {
            logger.error("Error when closing connection", e)
        }
    }

    /**
     * Closes a prepared statement.
     *
     * @param stmt [Statement] that should be closed
     */
    fun closeStatement(stmt: Statement?) {
        try {
            stmt?.close()
        } catch (e: SQLException) {
            logger.error("Couldn't close statement", e)
        }
    }

    private fun getDatabaseConnectionUrl(properties: Properties): String = properties.getProperty("jdbc.url")

    private fun getDatabaseUsername(properties: Properties): String = properties.getProperty("jdbc.username")

    private fun getDatabasePassword(properties: Properties): String = properties.getProperty("jdbc.password")

    private fun getDrivers(properties: Properties): String = properties.getProperty("jdbc.driverClassName")
}
