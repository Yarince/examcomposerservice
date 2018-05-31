package nl.han.ica.examplatform.persistence.answer

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.responseexceptions.DatabaseException
import nl.han.ica.examplatform.controllers.responseexceptions.InvalidAnswerException
import nl.han.ica.examplatform.models.answermodel.answer.Answer
import nl.han.ica.examplatform.models.exam.Exam
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
    override fun addOrUpdateAnswerInQuestion(answer: Answer) {
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

    /**
     * Add or Update an Answer for a [Question] in an [Exam] in the database
     *
     * @param answer [Answer] The Answer you want to add to a Question in an Exam
     * @param examId [Int] The ID of the exam you want to add the Answer to
     */
    override fun addOrUpdateAnswerInQuestionInExam(answer: Answer, examId: Int) {
        if (answer.partialAnswers == null || answer.partialAnswers.size < 1)
            throw InvalidAnswerException("Please provide partialAnswers to for question")

        val paInQeQuery = "INSERT INTO PARTIAL_ANSWER_IN_QUESTION_IN_EXAM (PARTIALANSWERID, QUESTIONID, EXAMID, POINTS) value (?,?,?,?) on DUPLICATE KEY UPDATE POINTS = ?"

        val conn: Connection? = MySQLConnection.getConnection()
        val preparedStatement = conn?.prepareStatement(paInQeQuery)

        for (partialAnswer in answer.partialAnswers) {

            preparedStatement?.setInt(1, partialAnswer.partialAnswerId
                    ?: throw InvalidAnswerException("PartialAnswerId is not set"))

            preparedStatement?.setInt(2, answer.questionId)

            preparedStatement?.setInt(3, examId)

            preparedStatement?.setInt(4, partialAnswer.points)
            preparedStatement?.setInt(5, partialAnswer.points)
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

    override fun getAnswerForQuestion(questionId: Int): ArrayList<Answer> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAnswersForExam(questionId: Int): ArrayList<Answer> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
