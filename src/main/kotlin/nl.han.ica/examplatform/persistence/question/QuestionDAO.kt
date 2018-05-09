package nl.han.ica.examplatform.persistence.question

import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Database access object that handles all database queries regarding [Question]
 */
@Repository
class QuestionDAO {

    /**
     * Adds a question to the database
     */
    fun insertQuestion(question: Question): Question {
        var dbConnection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        val sqlQueryStringInsertQuestionString = "INSERT INTO QUESTION (QUESTIONID, PARENTQUESTIONID, EXAMTYPEID, COURSEID, QUESTIONTEXT, QUESTIONTYPE, SEQUENCENUMBER, ANSWERTEXT, ANSWERKEYWORDS, ASSESSMENTCOMMENTS) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(sqlQueryStringInsertQuestionString)
            preparedStatement?.setInt(1, question.questionId ?: 0)
            if (question.parentQuestionId == null) preparedStatement?.setNull(2, java.sql.Types.INTEGER) else preparedStatement?.setInt(2, question.parentQuestionId)
            preparedStatement?.setInt(3, question.examTypeId.examId)
            preparedStatement?.setInt(4, question.courseId)
            preparedStatement?.setString(5, question.questionText)
            preparedStatement?.setString(6, question.questionType.toString())
            if (question.sequenceNumber == null) preparedStatement?.setNull(7, java.sql.Types.INTEGER) else preparedStatement?.setInt(7, question.sequenceNumber)
            preparedStatement?.setString(8, question.answerText)
            preparedStatement?.setString(9, question.answerKeywords)
            preparedStatement?.setString(10, question.assessmentComments)
            preparedStatement?.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            MySQLConnection.closeStatement(preparedStatement)
        }
        return question
    }

    /**
     * Checks if a question already exists in the database
     */
    fun exists(question: Question?): Boolean {
        var dbConnection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        val sqlQueryStringSelectIfQuestionExistsString = "SELECT QUESTIONTEXT FROM QUESTION WHERE QUESTIONID = ?"
        try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(sqlQueryStringSelectIfQuestionExistsString)
            preparedStatement?.setInt(1, question?.questionId ?: 0)
            val rs = preparedStatement?.executeQuery()
            if (rs?.next() == true) {
                return true
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            preparedStatement?.close()
        }
        return false
    }
}