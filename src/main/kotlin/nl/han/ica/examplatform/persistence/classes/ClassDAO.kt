package nl.han.ica.examplatform.persistence.classes

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.DatabaseException
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.SQLException

/**
 * This class handles all the Database operations for the retrieval of classes.
 */
@Repository
class ClassDAO : IClassDAO {

    private val logger = loggerFor(javaClass)

    override fun getAllClasses(): ArrayList<String> {
        val classes: ArrayList<String> = ArrayList()
        val conn: Connection? = MySQLConnection.getConnection()
        val sqlGetAllClassesQuery = "SELECT CLASSNAME FROM CLASSES"
        val preparedStatementExam = conn?.prepareStatement(sqlGetAllClassesQuery)

        return try {
            val rs = preparedStatementExam?.executeQuery()
                    ?: throw DatabaseException("Error while trying to retrieve all the classes.")
            while (rs.next()) {
                classes.add(rs.getString("CLASSNAME"))
            }
            classes
        } catch (e: SQLException) {
            logger.error("Error retrieving classes", e)
            throw DatabaseException("Error while retrieving all classes", e)
        }
    }
}