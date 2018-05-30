package nl.han.ica.examplatform.persistence.answer

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.models.answerModel.answer.Answer
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * The DAO class for [Answer]
 */
@Repository
class AnswerDAO : IAnswerDAO {
    private val logger = loggerFor(javaClass)

    /**
     * Add an Answer to a Question in the database
     *
     * @param answer The [Answer] you want to add to a [Question]
     */
    override fun addAnswerToQuestion(answer: Answer) {
        val insertAnswerQuery = "UPDATE QUESTION SET ANSWERTEXT = ?, ANSWERKEYWORDS = ? WHERE QUESTIONID = ?"
        var dbConnection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(insertAnswerQuery)
            preparedStatement?.setString(2, answer.answerKeywords.toString())
            preparedStatement?.setInt(3, answer.questionId)
            preparedStatement?.executeUpdate()
        } catch (e: SQLException) {
            logger.error("SQLException thrown when adding answer to question", e)
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            preparedStatement?.close()
        }
    }
}