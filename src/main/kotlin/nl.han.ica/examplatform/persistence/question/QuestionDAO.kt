package nl.han.ica.examplatform.persistence.question

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.responseexceptions.DatabaseException
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import org.springframework.util.MultiValueMap
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Database access object that handles all database queries regarding [Question].
 */
@Repository
class QuestionDAO : IQuestionDAO {

    private val logger = loggerFor(javaClass)

    /**
     * Adds a question to the database.
     *
     * @param question [Question] The question to be added.
     * @return [Question] the inserted question
     */
    override fun insertQuestion(question: Question, parentQuestionId: Int?): Question {
        var questionToReturn = question
        var dbConnection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        val sqlQueryStringInsertQuestionString = "INSERT INTO QUESTION (QUESTIONTEXT, QUESTIONTYPE, COURSEID, PARENTQUESTIONID, EXAMTYPENAME, PLUGINVERSION) VALUES (?, ?, ?, ?, ?,?)"
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

            preparedStatement?.setString(5, question.examType)
            preparedStatement?.setString(6, question.pluginVersion)

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
    override fun exists(question: Question?): Boolean {
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
    override fun getQuestionsByCourse(courseId: Int): Array<Question> {
        val conn: Connection? = MySQLConnection.getConnection()
        var preparedQuestionStatement: PreparedStatement? = null

        val sqlQuestionQuery = "SELECT distinct Q.QUESTIONID, QE.SEQUENCENUMBER, QE.QUESTIONID, QUESTIONTYPE, QUESTIONTEXT, QUESTIONPOINTS, COURSEID, EXAMTYPENAME FROM QUESTION as Q INNER JOIN QUESTION_IN_EXAM as QE ON Q.QUESTIONID = QE.QUESTIONID WHERE COURSEID = ? and PARENTQUESTIONID is null"

        val sqlSubQuestionQuery = "SELECT Q.QUESTIONID, QE.SEQUENCENUMBER, QE.QUESTIONID, QUESTIONTYPE, QUESTIONTEXT, QUESTIONPOINTS, COURSEID, EXAMTYPENAME  FROM QUESTION as Q left JOIN QUESTION_IN_EXAM as QE ON Q.QUESTIONID = QE.QUESTIONID WHERE PARENTQUESTIONID = ?"

        var questions = ArrayList<Question>()
        try {
            preparedQuestionStatement = conn?.prepareStatement(sqlQuestionQuery)
            preparedQuestionStatement?.setInt(1, courseId)


            questions = initQuestionsByResultSet(preparedQuestionStatement, sqlSubQuestionQuery, conn)

        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedQuestionStatement)
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
    override fun getQuestionsByCourseAndCategory(courseId: Int, categories: Array<String>): Array<Question> {
        val conn: Connection? = MySQLConnection.getConnection()
        var preparedStatement: PreparedStatement? = null

        var queryGetQuestions = "SELECT * FROM QUESTION Q INNER JOIN CATEGORIES_OF_QUESTION COQ ON Q.QUESTIONID = COQ.QUESTIONID INNER JOIN CATEGORY C ON C.CATEGORYID = COQ.CATEGORYID WHERE COURSEID = ? "
        val sqlSubQuestionQuery = "SELECT Q.QUESTIONID, QE.SEQUENCENUMBER, QE.QUESTIONID, QUESTIONTYPE, QUESTIONTEXT, QUESTIONPOINTS, COURSEID, EXAMTYPENAME  FROM QUESTION as Q left JOIN QUESTION_IN_EXAM as QE ON Q.QUESTIONID = QE.QUESTIONID WHERE PARENTQUESTIONID = ?"

        for ((index, _) in categories.withIndex()) {
            queryGetQuestions += when (index) {
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

                questions.add(Question(questionId = questionRs.getInt("QuestionID"),
                        questionType = questionRs.getString("QuestionType"),
                        questionText = questionRs.getString("QuestionText"),
                        examType = questionRs.getString("EXAMTYPENAME"),
                        categories = getCategoriesOfQuestion(questionRs.getInt("QuestionID"), conn),
                        subQuestions = getSubQuestionsOfQuestion(questionRs.getInt("QuestionID"), conn, sqlSubQuestionQuery),
                        pluginVersion = questionRs.getString("PLUGINVERSION")))
            }
        } catch (e: SQLException) {
            logger.error("Something went wrong while getting all courses", e)
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedStatement)
        }
        return questions.toTypedArray()
    }

    /**
     * Gets all questions of a course, within specific categories
     *
     * @param examId [Int] The ID of exam of which the questions should be retrieved
     * @return [Array]<[Question]> An array of all questions corresponding to the course and categories
     */
    override fun getQuestionsByExam(examId: Int): ArrayList<Question> {
        val conn: Connection? = MySQLConnection.getConnection()
        var preparedQuestionStatement: PreparedStatement? = null

        val sqlQuestionQuery = "SELECT distinct Q.QUESTIONID, QE.SEQUENCENUMBER, QE.QUESTIONID, QUESTIONTYPE, QUESTIONTEXT, QUESTIONPOINTS, COURSEID, EXAMTYPENAME FROM QUESTION as Q INNER JOIN QUESTION_IN_EXAM as QE ON Q.QUESTIONID = QE.QUESTIONID WHERE EXAMID = ? and PARENTQUESTIONID is null"

        val sqlSubQuestionQuery = "SELECT Q.QUESTIONID, QE.SEQUENCENUMBER, QE.QUESTIONID, QUESTIONTYPE, QUESTIONTEXT, QUESTIONPOINTS, COURSEID, EXAMTYPENAME  FROM QUESTION as Q JOIN QUESTION_IN_EXAM as QE ON Q.QUESTIONID = QE.QUESTIONID WHERE PARENTQUESTIONID = ?"

        val questions: ArrayList<Question>
        try {
            preparedQuestionStatement = conn?.prepareStatement(sqlQuestionQuery)
            preparedQuestionStatement?.setInt(1, examId)

            questions = initQuestionsByResultSet(preparedQuestionStatement, sqlSubQuestionQuery, conn)

        } catch (e: SQLException) {
            logger.error("Something went wrong while getting all courses", e)
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedQuestionStatement)
        }

        return questions
    }

    private fun initQuestionsByResultSet(preparedQuestionStatement: PreparedStatement?, sqlSubQuestionQuery: String, conn: Connection?): ArrayList<Question> {
        val questions = ArrayList<Question>()

        val questionRs = preparedQuestionStatement?.executeQuery()
                ?: throw DatabaseException("Error while interacting with the database")

        while (questionRs.next())
            questions.add(Question(questionId = questionRs.getInt("QUESTIONID"),
                    questionOrderInExam = questionRs.getInt("SEQUENCENUMBER"),
                    questionOrderText = questionRs.getString("SEQUENCENUMBER"),
                    questionType = questionRs.getString("QUESTIONTYPE"),
                    questionText = questionRs.getString("QUESTIONTEXT"),
                    questionPoints = questionRs.getFloat("QUESTIONPOINTS"),
                    courseId = questionRs.getInt("COURSEID"),
                    examType = questionRs.getString("EXAMTYPENAME"),
                    categories = getCategoriesOfQuestion(questionRs.getInt("QUESTIONID"), conn),
                    subQuestions = getSubQuestionsOfQuestion(questionRs.getInt("QUESTIONID"), conn, sqlSubQuestionQuery),
                    pluginVersion = questionRs.getString("PLUGINVERSION")
            ))
        return questions
    }

    private fun getSubQuestionsOfQuestion(questionId: Int, conn: Connection?, sqlSubQuestionQuery: String): ArrayList<Question>? {
        var preparedQuestionStatement: PreparedStatement? = null

        val questions: ArrayList<Question>
        try {
            preparedQuestionStatement = conn?.prepareStatement(sqlSubQuestionQuery)
            preparedQuestionStatement?.setInt(1, questionId)

            questions = initQuestionsByResultSet(preparedQuestionStatement, sqlSubQuestionQuery, conn)

        } catch (e: SQLException) {
            logger.error("Something went wrong while getting all courses", e)
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeStatement(preparedQuestionStatement)
        }

        return questions
    }

    private fun getCategoriesOfQuestion(questionId: Int, conn: Connection?): ArrayList<String> {
        var preparedQuestionCategoryStatement: PreparedStatement? = null

        val sqlQuestionCategoryQuery = "SELECT CATEGORYNAME FROM CATEGORIES_OF_QUESTION as CQ INNER JOIN CATEGORY as C ON CQ.CATEGORYID = C.CATEGORYID WHERE QUESTIONID = ?"

        val questions = ArrayList<String>()
        try {
            preparedQuestionCategoryStatement = conn?.prepareStatement(sqlQuestionCategoryQuery)
            preparedQuestionCategoryStatement?.setInt(1, questionId)

            val questionCategoryRs = preparedQuestionCategoryStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")

            while (questionCategoryRs.next())
                questions.add(questionCategoryRs.getString("CATEGORYNAME"))

        } catch (e: SQLException) {
            logger.error("Something went wrong while getting all courses", e)
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeStatement(preparedQuestionCategoryStatement)
        }
        return questions
    }


    /**
     * Get a question by questionId.
     *
     * @param questionId [Int] The ID of the question which should be retrieved.
     * @return [Question] Question corresponding to the ID.
     */
    override fun getQuestionById(questionId: Int): Question {
        val conn: Connection? = MySQLConnection.getConnection()
        var preparedQuestionStatement: PreparedStatement? = null

        val sqlQuestionQuery = "SELECT distinct Q.QUESTIONID, QE.SEQUENCENUMBER, QE.QUESTIONID, QUESTIONTYPE, QUESTIONTEXT, QUESTIONPOINTS, COURSEID, EXAMTYPENAME FROM QUESTION as Q INNER JOIN QUESTION_IN_EXAM as QE ON Q.QUESTIONID = QE.QUESTIONID WHERE Q.QUESTIONID = ?"

        val sqlSubQuestionQuery = "SELECT Q.QUESTIONID, QE.SEQUENCENUMBER, QE.QUESTIONID, QUESTIONTYPE, QUESTIONTEXT, QUESTIONPOINTS, COURSEID, EXAMTYPENAME  FROM QUESTION as Q left JOIN QUESTION_IN_EXAM as QE ON Q.QUESTIONID = QE.QUESTIONID WHERE PARENTQUESTIONID = ?"

        val questions: ArrayList<Question>
        try {
            preparedQuestionStatement = conn?.prepareStatement(sqlQuestionQuery)
            preparedQuestionStatement?.setInt(1, questionId)

            questions = initQuestionsByResultSet(preparedQuestionStatement, sqlSubQuestionQuery, conn)

        } catch (e: SQLException) {
            logger.error("Something went wrong while getting all courses", e)
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedQuestionStatement)
        }

        return questions.first()

    }
}
