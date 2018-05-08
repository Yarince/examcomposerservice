package nl.han.ica.examplatform.persistence.answer

import nl.han.ica.examplatform.models.answer.Answer
import nl.han.ica.examplatform.models.answer.OpenAnswer
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
class AnswerDAO {

    var dbConnection : Connection? = null
    var preparedStatement : PreparedStatement? = null

    /**
     * This is used to add an Answer to a Question
     *
     * @param question The [Question] you want to add an [Answer] to
     */
    fun addOpenAnswerToQuestion(answer: Answer) {
        val insertAnswerQuery = "UPDATE QUESTION SET ANSWERTEXT = ?, ANSWERKEYWORDS = ? WHERE QUESTIONID = ${answer.questionId}"

        try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(insertAnswerQuery)
            preparedStatement?.setString(1, answer.answerText)
            preparedStatement?.setString(2, answer.answerKeywords.toString())
            preparedStatement?.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            MySQLConnection.closeStatement(preparedStatement)
        }
    }


}