package nl.han.ica.examplatform.persistence.question

import nl.han.ica.examplatform.controllers.responseexceptions.DatabaseException
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
    fun insertQuestion(question: Question, parentQuestionId: Int? = null): Question {
        var questionToReturn = question
        var dbConnection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        val sqlQueryStringInsertQuestionString = "INSERT INTO QUESTION (QUESTIONTEXT, QUESTIONTYPE, COURSEID, PARENTQUESTIONID, EXAMTYPEID) VALUES (?, ?, ?, ?, 1)"
        try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(sqlQueryStringInsertQuestionString)
            preparedStatement?.setString(1, question.questionText)
            preparedStatement?.setString(2, question.questionType)
            preparedStatement?.setInt(3, question.courseId ?: 1)
            if (parentQuestionId != null) preparedStatement?.setInt(4, parentQuestionId) else preparedStatement?.setNull(4, java.sql.Types.INTEGER)

            val insertedRows = preparedStatement?.executeUpdate()
            if (insertedRows == 1) {
                val idQuery = "SELECT LAST_INSERT_ID() AS ID"
                val idPreparedStatement = dbConnection?.prepareStatement(idQuery)
                val result = idPreparedStatement?.executeQuery()
                result?.let {
                    while (result.next()) {
                        questionToReturn = question.copy(questionId = result.getInt("ID"))
                    }
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw DatabaseException("Couldn't execute statement")
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            MySQLConnection.closeStatement(preparedStatement)
        }
        return questionToReturn
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
            print(e)
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            preparedStatement?.close()
        }
        return false
    }
}