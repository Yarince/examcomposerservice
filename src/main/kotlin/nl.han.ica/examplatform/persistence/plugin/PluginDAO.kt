package nl.han.ica.examplatform.persistence.plugin

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.responseexceptions.DatabaseException
import nl.han.ica.examplatform.models.plugin.Plugin
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * This class handles all the Database operations for [Plugin].
 */
@Repository
class PluginDAO {

    val logger = loggerFor(javaClass)

    /**
     * This function gets a list of all plugins from the database.
     *
     * @return [ArrayList]<[Plugin]> List of [Plugin]s
     */
    fun getAllPlugins(): ArrayList<Plugin> {
        val dbConnection: Connection? = MySQLConnection.getConnection()
        val dbQuery = "SELECT PluginId, PluginName, PluginVersion, PluginDescription FROM PLUGIN"
        val preparedStatement: PreparedStatement? = dbConnection?.prepareStatement(dbQuery)

        val result = arrayListOf<Plugin>()
        try {
            val rs = preparedStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")

            while (rs.next())
                result.add(Plugin(
                        rs.getInt("PluginId"),
                        rs.getString("PluginName"),
                        rs.getString("PluginVersion"),
                        rs.getString("PluginDescription")
                ))
        } catch (e: SQLException) {
            logger.error("Something went wrong while getting plugin list", e)
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
            MySQLConnection.closeConnection(dbConnection)
        }
        return result
    }
}
