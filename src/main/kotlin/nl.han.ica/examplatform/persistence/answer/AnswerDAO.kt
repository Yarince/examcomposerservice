package nl.han.ica.examplatform.persistence.answer

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.responseexceptions.DatabaseException
import nl.han.ica.examplatform.models.answermodel.answer.Answer
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.SQLException

/**
 * The DAO class for [Answer].
 */
@Repository
class AnswerDAO : IAnswerDAO {

    private val logger = loggerFor(javaClass)

    /**
     * Add an Answer to a Question in the database.
     *
     * @param answer The [Answer] you want to add to a [Question]
     */
    override fun addAnswerToQuestion(answer: Answer) {
        if (answer.partialAnswers == null || answer.partialAnswers.size < 1)
            throw DatabaseException("Please provide partialAnswers to for question")

        val query = "INSERT INTO PARTIAL_ANSWER (PARTIALANSWERID, QUESTIONID, PARTIALANSWERTEXT) value (?,?,?) on DUPLICATE KEY UPDATE PARTIALANSWERTEXT = ?"

        val conn: Connection? = MySQLConnection.getConnection()
        val preparedStatement = conn?.prepareStatement(query)

        for (partialAnswer in answer.partialAnswers) {

            partialAnswer.partialAnswerId?.let { preparedStatement?.setInt(1, it) }
                    ?: preparedStatement?.setNull(1, java.sql.Types.INTEGER)

            preparedStatement?.setInt(2, answer.questionId)

            preparedStatement?.setString(3, partialAnswer.partialAnswerText)
            preparedStatement?.setString(4, partialAnswer.partialAnswerText)
            try {
                preparedStatement?.executeUpdate()
                        ?: throw DatabaseException("Error while interacting with the database")
            } catch (e: SQLException) {
                logger.error("SQLException thrown when adding answer to question", e)
            }
        }

        MySQLConnection.closeConnection(conn)
        preparedStatement?.close()
    }
}
