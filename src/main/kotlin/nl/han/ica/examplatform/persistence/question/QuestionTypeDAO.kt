package nl.han.ica.examplatform.persistence.question

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.responseexceptions.DatabaseException
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * This class handles all the Database operations for question types.
 */
@Repository
class QuestionTypeDAO : IQuestionTypeDAO {
    private val logger = loggerFor(javaClass)

    /**
     * This function gets a list of all questionTypes from the database.
     *
     * @return [ArrayList]<[String]> List of questionTypes
     */
    override fun getAllQuestionTypes(): ArrayList<String> {
        val dbConnection: Connection? = MySQLConnection.getConnection()
        val pluginNameQuery = "SELECT PluginName FROM `PLUGIN`"
        val preparedStatement: PreparedStatement? = dbConnection?.prepareStatement(pluginNameQuery)

        val result = arrayListOf<String>()
        try {
            val rs = preparedStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")

            while (rs.next())
                result.add(rs.getString("PluginName"))

        } catch (e: SQLException) {
            logger.error("Error when retrieving questionTypes", e)
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
            MySQLConnection.closeConnection(dbConnection)
        }
        return result
    }
}