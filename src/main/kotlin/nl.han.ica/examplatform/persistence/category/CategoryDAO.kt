package nl.han.ica.examplatform.persistence.category

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.responseexceptions.DatabaseException
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Database Access Object for categories
 */
@Repository
class CategoryDAO : ICategoryDAO {
    override val logger = loggerFor(javaClass)

    /**
     * Gets all categories within questions of a course.
     *
     * @param courseId [Int] The ID of course of which the questions should be retrieved.
     * @return [ArrayList]<[String]> An array of all categories corresponding to the course.
     */
    override fun getCategoriesByCourse(courseId: Int): ArrayList<String> {
        val conn: Connection? = MySQLConnection.getConnection()
        var preparedQuestionStatement: PreparedStatement? = null

        val sqlQuestionQuery = "select C.CATEGORYNAME FROM CATEGORY C WHERE EXISTS( SELECT 1 FROM CATEGORIES_OF_QUESTION COQ WHERE COQ.CATEGORYID = C.CATEGORYID AND EXISTS( SELECT 1 FROM QUESTION Q WHERE Q.QUESTIONID = COQ.QUESTIONID))"

        val categories = ArrayList<String>()
        try {
            preparedQuestionStatement = conn?.prepareStatement(sqlQuestionQuery)
            preparedQuestionStatement?.setInt(1, courseId)

            val rs = preparedQuestionStatement?.executeQuery() ?: throw DatabaseException("Couldn't execute query")
            while (rs.next())
                categories.add(rs.getString(0))
        } catch (e: SQLException) {
            logger.error("Something went wrong while getting categories by course", e)
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedQuestionStatement)
        }

        return categories
    }
}
