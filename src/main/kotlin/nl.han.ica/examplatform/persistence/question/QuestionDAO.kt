package nl.han.ica.examplatform.persistence.question

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.responseexceptions.DatabaseException
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Database access object that handles all database queries regarding [Question].
 */
@Repository
class QuestionDAO {

    private val logger = loggerFor(javaClass)

    /**
     * Adds a question to the database.
     *
     * @param question [Question] The question to be added.
     * @return [Question] the inserted question
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
            if (parentQuestionId != null)
                preparedStatement?.setInt(4, parentQuestionId)
            else
                preparedStatement?.setNull(4, java.sql.Types.INTEGER)

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
            logger.error("Something went wrong while inserting a question in the database", e)
            throw DatabaseException("Couldn't execute statement")
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            MySQLConnection.closeStatement(preparedStatement)
        }
        return questionToReturn
    }

    /**
     * Checks if a question already exists in the database.
     *
     * @param question [Question] the question which should be checked on existing.
     * @return [Boolean] true if it exists, false if not.
     **/
    fun exists(question: Question?): Boolean {
        var dbConnection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        val queryCheckExists = "SELECT QUESTIONTEXT FROM QUESTION WHERE QUESTIONID = ?"
        try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(queryCheckExists)
            preparedStatement?.setInt(1, question?.questionId ?: 0)
            val rs = preparedStatement?.executeQuery()
            if (rs?.next() == true) {
                return true
            }
        } catch (e: SQLException) {
            logger.error("Something went wrong while checking if question ${question?.questionId} exists", e)
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            preparedStatement?.close()
        }
        return false
    }

    /**
     * Gets all questions of a course.
     *
     * @param courseId [Int] The ID of course of which the questions should be retrieved.
     * @return [Array]<[Question]> An array of all questions corresponding to the course.
     */
    fun getQuestions(courseId: Int): Array<Question> {
        val conn: Connection? = MySQLConnection.getConnection()
        var preparedStatement: PreparedStatement? = null

        val sqlQuery = "SELECT * FROM QUESTION Q WHERE COURSEID = ?"

        val questions = ArrayList<Question>()
        try {
            preparedStatement = conn?.prepareStatement(sqlQuery)
            preparedStatement?.setInt(1, courseId)

            val questionRs = preparedStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")

            while (questionRs.next())
                questions.add(Question(questionId = questionRs.getInt("QuestionID"),
                        questionText = questionRs.getString("QuestionText"),
                        questionType = questionRs.getString("QuestionType")))

        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedStatement)
        }
        return questions.toTypedArray()
    }

    /**
     * Gets all questions of a course, within specific categories
     *
     * @param courseId [Int] The ID of course of which the questions should be retrieved
     * @param categories [Array] An array containing all the categories of which the questions should be retrieved
     * @return [Array]<[Question]> An array of all questions corresponding to the course and categories
     */
    fun getQuestions(courseId: Int, categories: Array<String>): Array<Question> {
        val conn: Connection? = MySQLConnection.getConnection()
        var preparedStatement: PreparedStatement? = null

        var queryGetQuestions = "SELECT * FROM QUESTION Q INNER JOIN CATEGORIES_OF_QUESTION COQ ON Q.QUESTIONID = COQ.QUESTIONID INNER JOIN CATEGORY C ON C.CATEGORYID = COQ.CATEGORYID WHERE COURSEID = ? "

        for ((index, _) in categories.withIndex()) {
            queryGetQuestions += when(index) {
                0 -> "AND CATEGORYNAME = ?"
                else -> "OR CATEGORYNAME = ?"
            }
        }
        val questions = ArrayList<Question>()
        try {
            preparedStatement = conn?.prepareStatement(queryGetQuestions)
            preparedStatement?.setInt(1, courseId)

            for ((index, category) in categories.withIndex()) {
                preparedStatement?.setString(index + 2, category)
            }

            val questionRs = preparedStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")

            while (questionRs.next()) {
                val questionId = questionRs.getInt("QuestionID")
                val category = questionRs.getString("CategoryName")

                if (questions.any { it.questionId == questionId }) {
                    val question = questions.find { it.questionId ==  questionId }
                    question?.categories = question?.categories?.clone()!!
                    questions[questions.indexOf(question)] = question
                }

                questions.add(Question(questionId = questionRs.getInt("QuestionID"),
                        questionText = questionRs.getString("QuestionText"),
                        questionType = questionRs.getString("QuestionType"),
                        categories = arrayOf(category)))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedStatement)
        }
        return questions.toTypedArray()
    }

    /**
     * Get a question from the database by question ID.
     *
     * @param questionId [Int] The ID of Question which should be retrieved.
     * @return [Question] A question corresponding to the id.
     */
    fun getQuestion(questionId: Int): Question {
        return Question(questionId = 1,
                questionText = "Stub",
                questionType = "Stub")
    }
}
