package nl.han.ica.examplatform.persistence.course

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.DatabaseException
import nl.han.ica.examplatform.models.course.Course
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * This class handles all the Database operations for [Course]s
 */
@Repository
class CourseDAO {

    val logger = loggerFor(javaClass)

    /**
     * This function gets a list of all courses from the database.
     *
     * @return [ArrayList] of [Course]s
     */
    fun getAllCourses(): ArrayList<Course> {
        val dbConnection: Connection? = MySQLConnection.getConnection()
        val dbQuery = "SELECT COURSEID, COURSENAME, COURSECODE FROM COURSE"
        val preparedStatement: PreparedStatement? = dbConnection?.prepareStatement(dbQuery)

        val result = arrayListOf<Course>()
        try {
            val rs = preparedStatement?.executeQuery() ?: throw DatabaseException("Prepared statement couldn't be made")

            while (rs.next())
                result.add(Course(
                        rs.getInt("COURSEID"),
                        rs.getString("COURSENAME"),
                        rs.getString("COURSECODE")
                ))
        } catch (e: SQLException) {
            val message = "Something went wrong while getting all courses"
            logger.error(message, e)
            throw DatabaseException(message, e)
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
            MySQLConnection.closeConnection(dbConnection)
        }
        return result
    }
}