package nl.han.ica.examplatform.persistence.answer

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.DatabaseException
import nl.han.ica.examplatform.controllers.question.QuestionNotFoundException
import nl.han.ica.examplatform.models.answermodel.AnswerModel
import nl.han.ica.examplatform.models.answermodel.answer.Answer
import nl.han.ica.examplatform.models.answermodel.answer.PartialAnswer
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
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
        if (answer.partial_answers == null || answer.partial_answers.size < 1)
            throw DatabaseException("Please provide partial_answers to for question")

        val query = "INSERT INTO PARTIAL_ANSWER (PARTIALANSWERID, QUESTIONID, PARTIALANSWERTEXT) value (?,?,?) on DUPLICATE KEY UPDATE PARTIALANSWERTEXT = ?"

        val conn: Connection? = MySQLConnection.getConnection()
        val preparedStatement = conn?.prepareStatement(query)

        for (partialAnswer in answer.partial_answers) {

            partialAnswer.id?.let { preparedStatement?.setInt(1, it) }
                    ?: preparedStatement?.setNull(1, java.sql.Types.INTEGER)

            preparedStatement?.setInt(2, answer.questionId)

            preparedStatement?.setString(3, partialAnswer.text)
            preparedStatement?.setString(4, partialAnswer.text)
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
        if (answer.partial_answers == null || answer.partial_answers.size < 1)
            throw DatabaseException("Please provide partial_answers to for question")

        val paInQeQuery = "INSERT INTO PARTIAL_ANSWER_IN_QUESTION_IN_EXAM (PARTIALANSWERID, QUESTIONID, EXAMID, POINTS) value (?,?,?,?) on DUPLICATE KEY UPDATE POINTS = ?"

        val conn: Connection? = MySQLConnection.getConnection()
        val preparedStatement = conn?.prepareStatement(paInQeQuery)

        for (partialAnswer in answer.partial_answers) {

            preparedStatement?.setInt(1, partialAnswer.id
                    ?: throw DatabaseException("PartialAnswerId is not set"))

            preparedStatement?.setInt(2, answer.questionId)

            preparedStatement?.setInt(3, examId)

            partialAnswer.points?.let { preparedStatement?.setInt(4, it) }
                    ?: preparedStatement?.setNull(4, java.sql.Types.INTEGER)
            partialAnswer.points?.let { preparedStatement?.setInt(5, it) }
                    ?: preparedStatement?.setNull(5, java.sql.Types.INTEGER)
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
     * Retrieve an Answer for a question from the database
     *
     * @param questionId [Int] The id of the question
     */
    override fun getAnswerForQuestion(questionId: Int): Answer {
        val conn: Connection? = MySQLConnection.getConnection()
        var preparedQuestionStatement: PreparedStatement? = null
        val preparedQuestionStatement2: PreparedStatement?

        val sqlQuestionQuery = "SELECT ANSWERTEXT FROM QUESTION WHERE QUESTIONID = ?"

        val sqlPartialQuery = """SELECT PA.PARTIALANSWERTEXT, PA.PARTIALANSWERID, PAIQIE.POINTS
                                 FROM PARTIAL_ANSWER PA JOIN PARTIAL_ANSWER_IN_QUESTION_IN_EXAM PAIQIE ON
                                 PA.PARTIALANSWERID = PAIQIE.PARTIALANSWERID
                                 WHERE PA.PARTIALANSWERID = ?;"""

        return try {
            preparedQuestionStatement = conn?.prepareStatement(sqlPartialQuery)
            preparedQuestionStatement2 = conn?.prepareStatement(sqlQuestionQuery)
            preparedQuestionStatement2?.setInt(1, questionId)
            preparedQuestionStatement?.setInt(1, questionId)

            val questionRs = preparedQuestionStatement2?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")

            val answerRs = preparedQuestionStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")

            val partialAnswers = ArrayList<PartialAnswer>()

            while (answerRs.next())
                partialAnswers.add(
                        PartialAnswer(
                                id = answerRs.getInt("PARTIALANSWERID"),
                                text = answerRs.getString("PARTIALANSWERTEXT"),
                                points = answerRs.getInt("POINTS")
                        )
                )

            questionRs.last()

            // This is the row of of the last result, so if this is smaller than 0
            if (questionRs.row < 1) throw QuestionNotFoundException("Question with ID $questionId was not found")

            Answer(
                    questionId = questionId,
                    partial_answers = partialAnswers
            )
        } catch (e: SQLException) {
            logger.error("SQLException thrown when adding answer to question", e)
            throw DatabaseException("")
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedQuestionStatement)
        }
    }

    /**
     * Get answerModel from database by [Exam]
     *
     * @param examId [Int] The ID of the exam
     *
     * @return [AnswerModel]
     */
    override fun getAnswersForExam(examId: Int): AnswerModel {
        val conn: Connection? = MySQLConnection.getConnection()


        val sqlAnswerQuery = """SELECT PARTIALANSWERTEXT, PA.PARTIALANSWERID, PAIQIE.POINTS, ANSWERTEXT, Q.QUESTIONID
                              FROM PARTIAL_ANSWER PA
                              JOIN PARTIAL_ANSWER_IN_QUESTION_IN_EXAM PAIQIE ON PA.PARTIALANSWERID = PAIQIE.PARTIALANSWERID
                              JOIN QUESTION Q on PA.QUESTIONID = Q.QUESTIONID
                              WHERE PAIQIE.EXAMID = ?"""


        val sqlQuestionQuery = "SELECT ANSWERTEXT, Q.QUESTIONID FROM QUESTION_IN_EXAM QE LEFT JOIN QUESTION Q on QE.QUESTIONID = Q.QUESTIONID where QE.EXAMID = ?"

        val preparedAnswerStatement = conn?.prepareStatement(sqlAnswerQuery)
        val preparedQuestionStatement = conn?.prepareStatement(sqlQuestionQuery)

        preparedAnswerStatement?.setInt(1, examId)
        preparedQuestionStatement?.setInt(1, examId)

        return try {

            val answerRs = preparedAnswerStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")
            val questionRs = preparedQuestionStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")

            val answers = ArrayList<Answer>()
            var partialAnswers = ArrayList<PartialAnswer>()

            while (questionRs.next()) {
                val questionId = questionRs.getInt("QUESTIONID")
                while (answerRs.next()) {
                    val newQuestionId = answerRs.getInt("QUESTIONID")
                    if (questionId != newQuestionId) {
                        answerRs.previous()
                        break
                    }

                    partialAnswers.add(PartialAnswer(
                            id = answerRs.getInt("PARTIALANSWERID"),
                            text = answerRs.getString("PARTIALANSWERTEXT"),
                            points = answerRs.getInt("POINTS")

                    ))
                }

                answers.add(Answer(
                        questionId = questionId,
                        example_answer = questionRs.getString("ANSWERTEXT"),
                        partial_answers = partialAnswers
                ))

                partialAnswers = ArrayList()
            }

            AnswerModel(
                    examId = examId,
                    answerModelId = null,
                    answers = answers
            )
        } catch (e: SQLException) {
            logger.error("SQLException thrown when adding answer to question", e)
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedAnswerStatement)
            MySQLConnection.closeStatement(preparedQuestionStatement)
        }
    }
}
