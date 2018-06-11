package nl.han.ica.examplatform.persistence.category

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.DatabaseException
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.*

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

        val sqlCategoryQuery = """
            SELECT
                C.CATEGORYNAME
            FROM CATEGORY C
            WHERE
            EXISTS(
                SELECT 1
                FROM CATEGORIES_OF_QUESTION COQ
                WHERE COQ.CATEGORYID = C.CATEGORYID
                AND EXISTS(
                    SELECT 1
                    FROM QUESTION Q
                    WHERE Q.QUESTIONID = COQ.QUESTIONID
                    AND COURSEID = ?
                )
            )"""

        val categories = ArrayList<String>()
        try {
            preparedQuestionStatement = conn?.prepareStatement(sqlCategoryQuery)
            preparedQuestionStatement?.setInt(1, courseId)

            val rs = preparedQuestionStatement?.executeQuery() ?: throw DatabaseException("Couldn't execute query")
            while (rs.next())
                categories.add(rs.getString("CATEGORYNAME"))
        } catch (e: SQLException) {
            logger.error("Something went wrong while getting categories by course", e)
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedQuestionStatement)
        }

        return categories
    }

    /**
     * Adds categories to a question.
     *
     * @param categories [ArrayList]<[String]> The categories to add
     * @param questionId [Int] The ID of the question of which the categories should be added to
     */
    override fun addCategoriesToQuestion(categories: ArrayList<String>, questionId: Int) {
        val conn: Connection? = MySQLConnection.getConnection()
        var preparedQuestionStatement: PreparedStatement? = null
        var insertStatement: PreparedStatement? = null

        var sqlCategoryQuery = """
            SELECT
                C.CATEGORYID
            FROM CATEGORY C
            WHERE CATEGORYNAME in ("""

        // Add prepared statement parameters dynamically for all categories
        sqlCategoryQuery = addDynamicParameters(sqlCategoryQuery, categories)

        val insertQuery = "INSERT INTO CATEGORIES_OF_QUESTION (QUESTIONID, CATEGORYID) VALUES (?, ?)"

        val categoryIds = ArrayList<Int>()
        try {
            preparedQuestionStatement = conn?.prepareStatement(sqlCategoryQuery)

            // Set parameters in prepared statement
            addParameters(preparedQuestionStatement, categories)

            val rs = preparedQuestionStatement?.executeQuery() ?: throw DatabaseException("Couldn't execute query")

            while (rs.next())
                categoryIds.add(rs.getInt(1))

            if (categoryIds.size != categories.size) throw DatabaseException("No categories found with names: $categories")

            conn?.autoCommit = false
            insertStatement = conn?.prepareStatement(insertQuery)

            for (id: Int in categoryIds) {
                insertStatement?.setInt(1, questionId)
                insertStatement?.setInt(2, id)
                insertStatement?.addBatch()
            }

            insertStatement?.executeBatch()
                    ?: throw DatabaseException("Couldn't insert category batch")

            conn?.commit()

        } catch (e: BatchUpdateException) {
            logger.error("Something went wrong while getting categories by course.", e)
        } catch (e: SQLException) {
            logger.error("Something went wrong while getting categories by course.", e)
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedQuestionStatement)
            MySQLConnection.closeStatement(insertStatement)
        }
    }

    /**
     * Checks if a list with categories exists in the database.
     *
     * @param categories [ArrayList]<[String]> The categories to check
     * @return [Boolean] Returns true if they all exist, false if they don't
     */
    override fun checkIfCategoriesExist(categories: ArrayList<String>): Boolean {
        val conn: Connection? = MySQLConnection.getConnection()
        var preparedQuestionStatement: PreparedStatement? = null

        var sqlCategoryQuery = """
            SELECT
                C.CATEGORYID
            FROM CATEGORY C
            WHERE CATEGORYNAME in ("""

        // Add prepared statement parameters dynamically for all categories
        sqlCategoryQuery = addDynamicParameters(sqlCategoryQuery, categories)

        return try {
            preparedQuestionStatement = conn?.prepareStatement(sqlCategoryQuery)

            // Set parameters in prepared statement
            addParameters(preparedQuestionStatement, categories)

            val rs = preparedQuestionStatement?.executeQuery() ?: throw DatabaseException("Couldn't execute query")

            rs.last()
            rs.row == (categories.size)
        } catch (e: SQLException) {
            logger.error("Something went wrong while getting categories by course.", e)
            false
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedQuestionStatement)
        }
    }

    private fun addDynamicParameters(query: String, parameters: ArrayList<String>): String {
        val queryBuilder = StringBuilder(query)

        queryBuilder.append("?")
        for (i in 1 until parameters.size) {
            queryBuilder.append(", ?")
        }
        queryBuilder.append(") ")
        return queryBuilder.toString()
    }


    private fun addParameters(statement: PreparedStatement?, parameters: ArrayList<String>) {
        for ((i, param) in parameters.withIndex()) {
            statement?.setString(i + 1, param)
        }
    }
}
