package nl.han.ica.examplatform.persistence.exam.results

import nl.han.ica.examplatform.business.exam.practice.Results
import nl.han.ica.examplatform.business.exam.practice.models.QuestionResult
import nl.han.ica.examplatform.business.exam.practice.models.QuestionResultStats
import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.DatabaseException
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

@Repository
class ExamResultsDAO: IExamResultsDAO {
    private val logger = loggerFor(javaClass)

    override fun getPreviousResultsOfStudent(studentId: Int, courseId: Int): ArrayList<Results> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResultsOfOthersInCategory(studentId: Int, category: String): ArrayList<QuestionResultStats> {
        var dbConnection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        val query = "SELECT QUESTIONTEXT FROM QUESTION WHERE COURSEID = ?"
        return try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(query)
            preparedStatement?.setInt(1, studentId)
            preparedStatement?.setString(2, category)
            val rs = preparedStatement?.executeQuery() ?: throw DatabaseException("Couldn't execute statement")
            val results = ArrayList<QuestionResultStats>()
            while (rs.next()) {
                results.add(QuestionResultStats(
                        rs.getInt("QUESTIONID"),
                        rs.getInt("NRESULTS"),
                        rs.getInt("NCORRECT"),
                        rs.getInt("NWRONG")))
            }
            results
        } catch (e: SQLException) {
            val message = "Something went wrong while getting results of other students"
            logger.error(message, e)
            throw DatabaseException(message, e)
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            preparedStatement?.close()
        }
    }

    override fun getQuestionsAnsweredByStudentInCourse(studentId: Int, courseId: Int): ArrayList<QuestionResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}