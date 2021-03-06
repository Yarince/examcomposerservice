package nl.han.ica.examplatform.persistence.question

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.DatabaseException
import nl.han.ica.examplatform.models.answermodel.answer.PartialAnswer
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.*
import java.sql.SQLException
import java.util.*

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
        var dbConnection: Connection? = null
        var preparedStatementQuestion: PreparedStatement? = null
        val preparedStatementPartialAnswer: PreparedStatement?

        val sqlQueryStringInsertQuestionString = """
            INSERT INTO QUESTION (
                QUESTIONTEXT,
                QUESTIONTYPE,
                COURSEID,
                PARENTQUESTIONID,
                EXAMTYPENAME,
                QUESTIONTYPEPLUGINVERSION,
                ANSWERTYPE,
                PLUGINDATA,
                ANSWERTYPEPLUGINVERSION
                )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"""

        val sqlPartialAnswerQuery = """
           INSERT INTO PARTIAL_ANSWER (
               QUESTIONID,
               PARTIALANSWERTEXT
               )
           VALUES (?, ?)
        """
        try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatementQuestion = dbConnection?.prepareStatement(sqlQueryStringInsertQuestionString, Statement.RETURN_GENERATED_KEYS)
            preparedStatementPartialAnswer = dbConnection?.prepareStatement(sqlPartialAnswerQuery, Statement.RETURN_GENERATED_KEYS)

            preparedStatementQuestion?.setString(1, question.questionText)
            preparedStatementQuestion?.setString(2, question.questionType)
            preparedStatementQuestion?.setInt(3, question.courseId)
            if (parentQuestionId != null)
                preparedStatementQuestion?.setInt(4, parentQuestionId)
            else
                preparedStatementQuestion?.setNull(4, java.sql.Types.INTEGER)

            preparedStatementQuestion?.setString(5, question.examType)
            preparedStatementQuestion?.setString(6, question.questionTypePluginVersion)
            preparedStatementQuestion?.setString(7, question.answerType)
            preparedStatementQuestion?.setString(8, question.pluginData)
            preparedStatementQuestion?.setString(9, question.answerTypePluginVersion)

            preparedStatementQuestion?.executeUpdate()
                    ?: throw DatabaseException("Couldn't insert question")

            preparedStatementQuestion.generatedKeys.use {
                if (it.next()) {
                    val questionId = it.getInt(1)
                    question.questionId = questionId

                    question.partialAnswers.forEach {
                        preparedStatementPartialAnswer?.setInt(1, questionId)
                        preparedStatementPartialAnswer?.setString(2, it.text)
                        preparedStatementPartialAnswer?.addBatch()
                    }
                    preparedStatementPartialAnswer?.executeBatch()
                            ?: throw DatabaseException("Couldn't insert partial answer batch")

                    preparedStatementPartialAnswer.generatedKeys.use {
                        var iter = 0
                        while (it.next()) {
                            question.partialAnswers[iter++].id = it.getInt(1)
                        }
                    }
                }
            }
        } catch (e: SQLException) {
            val message = "Something went wrong while inserting a question in the database"
            logger.error(message, e)
            throw DatabaseException(message, e)
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            MySQLConnection.closeStatement(preparedStatementQuestion)
        }
        return question
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

        val queryCheckExists = "SELECT 1 FROM QUESTION WHERE QUESTIONID = ?"
        try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(queryCheckExists)
            preparedStatement?.setInt(1, question?.questionId ?: 0)
            val rs = preparedStatement?.executeQuery()
            if (rs?.next() == true) {
                return true
            }
        } catch (e: SQLException) {
            val message = "Something went wrong while checking if question ${question?.questionId} exists"
            logger.error(message, e)
            throw DatabaseException(message, e)
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

        val sqlQuestionQuery = """
            SELECT distinct
                QUESTIONID,
                QUESTIONTYPE,
                QUESTIONTEXT,
                COURSEID,
                EXAMTYPENAME,
                QUESTIONTYPEPLUGINVERSION,
                ANSWERTYPE,
                PLUGINDATA,
                ANSWERTYPEPLUGINVERSION
            FROM QUESTION
            WHERE
                COURSEID = ? and PARENTQUESTIONID is null;"""

        val sqlSubQuestionQuery = """
            SELECT
                QUESTIONID,
                QUESTIONID,
                QUESTIONTYPE,
                QUESTIONTEXT,
                COURSEID,
                EXAMTYPENAME,
                QUESTIONTYPEPLUGINVERSION,
                ANSWERTYPE,
                PLUGINDATA,
                ANSWERTYPEPLUGINVERSION
            FROM QUESTION
            WHERE PARENTQUESTIONID = ?;"""

        val questions: ArrayList<Question>
        try {
            preparedQuestionStatement = conn?.prepareStatement(sqlQuestionQuery)
            preparedQuestionStatement?.setInt(1, courseId)

            questions = initQuestionsByResultSet(preparedQuestionStatement, sqlSubQuestionQuery, conn)

        } catch (e: SQLException) {
            val message = "Question could not be retrieved from the database."
            logger.error(message, e)
            throw DatabaseException(message, e)
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedQuestionStatement)
        }

        if (questions.isEmpty()) throw DatabaseException("No questions found for course with ID: $courseId")

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
        var preparedStatementQuestion: PreparedStatement? = null

        var queryGetQuestions = """
            SELECT
                Q.QUESTIONID,
                Q.EXAMTYPENAME,
                COURSEID,
                QUESTIONTEXT,
                QUESTIONTYPE,
                ANSWERTYPE,
                ANSWERTYPEPLUGINVERSION,
                QUESTIONTYPEPLUGINVERSION,
                ANSWERTYPE,
                PLUGINDATA,
                ANSWERTYPEPLUGINVERSION
            FROM QUESTION Q INNER JOIN CATEGORIES_OF_QUESTION COQ ON
                Q.QUESTIONID = COQ.QUESTIONID
                INNER JOIN CATEGORY C
                ON C.CATEGORYID = COQ.CATEGORYID
            WHERE COURSEID = ? """

        val sqlSubQuestionQuery = """
            SELECT
                QUESTIONID,
                QUESTIONTYPE,
                QUESTIONTEXT,
                COURSEID,
                EXAMTYPENAME,
                ANSWERTYPE,
                PLUGINDATA,
                ANSWERTYPEPLUGINVERSION,
                QUESTIONTYPEPLUGINVERSION
            FROM QUESTION
            WHERE PARENTQUESTIONID = ?;"""

        for ((index, _) in categories.withIndex()) {
            queryGetQuestions += when (index) {
                0 -> "AND CATEGORYNAME = ?"
                else -> "OR CATEGORYNAME = ?"
            }
        }
        val questions = ArrayList<Question>()
        try {
            preparedStatementQuestion = conn?.prepareStatement(queryGetQuestions)
            preparedStatementQuestion?.setInt(1, courseId)

            for ((index, category) in categories.withIndex()) {
                preparedStatementQuestion?.setString(index + 2, category)
            }

            val questionRs = preparedStatementQuestion?.executeQuery()
                    ?: throw DatabaseException("Error while fetching questions from the database")

            while (questionRs.next()) {

                val questionId = questionRs.getInt("QuestionID")
                questions.add(Question(
                        questionId = questionId,
                        questionType = questionRs.getString("QUESTIONTYPE"),
                        questionText = questionRs.getString("QUESTIONTEXT"),
                        courseId = questionRs.getInt("COURSEID"),
                        examType = questionRs.getString("EXAMTYPENAME"),
                        answerType = questionRs.getString("ANSWERTYPE"),
                        answerTypePluginVersion = questionRs.getString("ANSWERTYPEPLUGINVERSION"),
                        pluginData = questionRs.getString("PLUGINDATA"),
                        questionTypePluginVersion = questionRs.getString("QUESTIONTYPEPLUGINVERSION"),
                        categories = getCategoriesOfQuestion(questionId, conn),
                        partialAnswers = getPartialAnswers(conn, questionId),
                        subQuestions = getSubQuestionsOfQuestion(questionId, conn, sqlSubQuestionQuery)
                ))
            }
        } catch (e: SQLException) {
            val message = "Question could not be retrieved from the database."
            logger.error(message, e)
            throw DatabaseException(message, e)
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedStatementQuestion)
        }

        if (questions.isEmpty()) throw DatabaseException("No questions found for course with ID: $courseId and categories: $categories")

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

        val sqlQuestionQuery = """
            SELECT distinct
                Q.QUESTIONID,
                QE.SEQUENCENUMBER,
                QE.QUESTIONID,
                Q.QUESTIONTYPE,
                Q.QUESTIONTEXT,
                QE.QUESTIONPOINTS,
                Q.COURSEID,
                Q.EXAMTYPENAME,
                Q.QUESTIONTYPEPLUGINVERSION,
                Q.ANSWERTYPE,
                Q.PLUGINDATA,
                Q.ANSWERTYPEPLUGINVERSION
            FROM QUESTION AS Q INNER JOIN QUESTION_IN_EXAM AS QE
                ON Q.QUESTIONID = QE.QUESTIONID
            WHERE EXAMID = ? AND PARENTQUESTIONID IS NULL"""

        val sqlSubQuestionQuery = """
            SELECT
                Q.QUESTIONID,
                QE.SEQUENCENUMBER,
                QE.QUESTIONID,
                Q.QUESTIONTYPE,
                QE.QUESTIONPOINTS,
                Q.QUESTIONTEXT,
                Q.COURSEID,
                Q.EXAMTYPENAME,
                Q.PLUGINDATA,
                Q.ANSWERTYPE,
                Q.ANSWERTYPEPLUGINVERSION,
                Q.QUESTIONTYPEPLUGINVERSION
            FROM QUESTION AS Q JOIN QUESTION_IN_EXAM AS QE
                ON Q.QUESTIONID = QE.QUESTIONID
            WHERE PARENTQUESTIONID = ? AND EXAMID = ?"""

        val questions = ArrayList<Question>()
        try {
            preparedQuestionStatement = conn?.prepareStatement(sqlQuestionQuery)
            preparedQuestionStatement?.setInt(1, examId)

            val questionRs = preparedQuestionStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")
            while (questionRs.next()) {

                val questionId = questionRs.getInt("QuestionID")
                questions.add(Question(questionId = questionId,
                        questionOrderInExam = questionRs.getInt("SEQUENCENUMBER"),
                        questionType = questionRs.getString("QUESTIONTYPE"),
                        questionText = questionRs.getString("QUESTIONTEXT"),
                        questionPoints = questionRs.getInt("QUESTIONPOINTS"),
                        courseId = questionRs.getInt("COURSEID"),
                        examType = questionRs.getString("EXAMTYPENAME"),
                        answerType = questionRs.getString("ANSWERTYPE"),
                        pluginData = questionRs.getString("PLUGINDATA"),
                        answerTypePluginVersion = questionRs.getString("ANSWERTYPEPLUGINVERSION"),
                        questionTypePluginVersion = questionRs.getString("QUESTIONTYPEPLUGINVERSION"),
                        categories = getCategoriesOfQuestion(questionRs.getInt("QUESTIONID"), conn),
                        partialAnswers = getPartialAnswers(conn, questionId, examId),
                        subQuestions = getSubQuestionsInExamOfQuestion(questionId, conn, sqlSubQuestionQuery, examId)
                ))
            }

        } catch (e: SQLException) {
            val message = "Question could not be retrieved from the database."
            logger.error(message, e)
            throw DatabaseException(message, e)
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

        while (questionRs.next()) {

            val questionId = questionRs.getInt("QuestionID")
            questions.add(Question(
                    questionId = questionId,
                    questionOrderInExam = null,
                    questionType = questionRs.getString("QUESTIONTYPE"),
                    questionText = questionRs.getString("QUESTIONTEXT"),
                    courseId = questionRs.getInt("COURSEID"),
                    examType = questionRs.getString("EXAMTYPENAME"),
                    answerType = questionRs.getString("ANSWERTYPE"),
                    answerTypePluginVersion = questionRs.getString("ANSWERTYPEPLUGINVERSION"),
                    questionTypePluginVersion = questionRs.getString("QUESTIONTYPEPLUGINVERSION"),
                    pluginData = questionRs.getString("PLUGINDATA"),
                    categories = getCategoriesOfQuestion(questionId, conn),
                    partialAnswers = getPartialAnswers(conn, questionId),
                    subQuestions = getSubQuestionsOfQuestion(questionId, conn, sqlSubQuestionQuery)
            ))
        }
        return questions
    }

    private fun getPartialAnswers(conn: Connection?, questionId: Int, examId: Int? = null): ArrayList<PartialAnswer> {
        val preparedStatementPartialAnswer: PreparedStatement?

        val queryPartialAnswers = examId?.let {
            """
                    Select
                        PA.PARTIALANSWERID,
                        PARTIALANSWERTEXT,
                       (SELECT POINTS
                                FROM PARTIAL_ANSWER_IN_QUESTION_IN_EXAM PAIQIE
                                WHERE
                                    PA.PARTIALANSWERID = PAIQIE.PARTIALANSWERID
                                    AND PAIQIE.EXAMID = ?) AS POINTS
                    FROM PARTIAL_ANSWER PA
                    Where PA.QUESTIONID = ?
            """
        } ?: run {
            """
                SELECT
                  PARTIALANSWERID,
                  PARTIALANSWERTEXT,
                  NULL AS POINTS
                FROM PARTIAL_ANSWER
                WHERE QUESTIONID = ?
            """
        }

        preparedStatementPartialAnswer = conn?.prepareStatement(queryPartialAnswers)
        preparedStatementPartialAnswer?.setInt(1, questionId)
        if (examId != null) {
            preparedStatementPartialAnswer?.setInt(1, examId)
            preparedStatementPartialAnswer?.setInt(2, questionId)
        }

        val partialAnswerRs = preparedStatementPartialAnswer?.executeQuery()
                ?: throw DatabaseException("Error while fetching partial answers from the database")
        val partialAnswers = ArrayList<PartialAnswer>()
        while (partialAnswerRs.next()) {
            partialAnswers.add(PartialAnswer(
                    id = partialAnswerRs.getInt("PARTIALANSWERID"),
                    text = partialAnswerRs.getString("PARTIALANSWERTEXT"),
                    points = partialAnswerRs.getObject("POINTS") as Int?
            ))
        }
        return partialAnswers
    }

    private fun getSubQuestionsInExamOfQuestion(parentQuestionId: Int, conn: Connection?, sqlSubQuestionQuery: String, examId: Int? = null): ArrayList<Question>? {
        var preparedQuestionStatement: PreparedStatement? = null
        val questions = ArrayList<Question>()

        try {
            preparedQuestionStatement = conn?.prepareStatement(sqlSubQuestionQuery)
            preparedQuestionStatement?.setInt(1, parentQuestionId)
            if (examId != null) preparedQuestionStatement?.setInt(2, examId)

            val questionRs = preparedQuestionStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")
            while (questionRs.next()) {

                val questionId = questionRs.getInt("QUESTIONID")
                questions.add(Question(
                        questionId = questionId,
                        questionOrderInExam = questionRs.getInt("SEQUENCENUMBER"),
                        questionType = questionRs.getString("QUESTIONTYPE"),
                        questionText = questionRs.getString("QUESTIONTEXT"),
                        courseId = questionRs.getInt("COURSEID"),
                        questionPoints = questionRs.getInt("QUESTIONPOINTS"),
                        examType = questionRs.getString("EXAMTYPENAME"),
                        answerType = questionRs.getString("ANSWERTYPE"),
                        pluginData = questionRs.getString("PLUGINDATA"),
                        answerTypePluginVersion = questionRs.getString("ANSWERTYPEPLUGINVERSION"),
                        questionTypePluginVersion = questionRs.getString("QUESTIONTYPEPLUGINVERSION"),
                        categories = getCategoriesOfQuestion(questionId, conn),
                        partialAnswers = getPartialAnswers(conn, questionId, examId),
                        subQuestions = getSubQuestionsInExamOfQuestion(questionId, conn, sqlSubQuestionQuery, examId)
                ))
            }
        } catch (e: SQLException) {
            val message = "Subquestions"
            logger.error(message, e)
            throw DatabaseException(message, e)
        } finally {
            MySQLConnection.closeStatement(preparedQuestionStatement)
        }

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
            val message = "Getting subquestions of question went wrong with questionId $questionId"
            logger.error(message, e)
            throw DatabaseException(message, e)
        } finally {
            MySQLConnection.closeStatement(preparedQuestionStatement)
        }

        return questions
    }

    private fun getCategoriesOfQuestion(questionId: Int, conn: Connection?): ArrayList<String> {
        var preparedQuestionCategoryStatement: PreparedStatement? = null
        val sqlQuestionCategoryQuery = "SELECT CATEGORYNAME FROM CATEGORIES_OF_QUESTION as CQ INNER JOIN CATEGORY as C ON CQ.CATEGORYID = C.CATEGORYID WHERE QUESTIONID = ?"
        val categories = ArrayList<String>()

        try {
            preparedQuestionCategoryStatement = conn?.prepareStatement(sqlQuestionCategoryQuery)
            preparedQuestionCategoryStatement?.setInt(1, questionId)

            val questionCategoryRs = preparedQuestionCategoryStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")

            while (questionCategoryRs.next())
                categories.add(questionCategoryRs.getString("CATEGORYNAME"))

        } catch (e: SQLException) {
            val message = "Categories could not be retrieved form database for Question with ID: $questionId"
            logger.error(message, e)
            throw DatabaseException(message, e)
        } finally {
            MySQLConnection.closeStatement(preparedQuestionCategoryStatement)
        }

        return categories
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

        val sqlQuestionQuery = """
                SELECT DISTINCT
                    QUESTIONID,
                    QUESTIONTYPE,
                    QUESTIONTEXT,
                    COURSEID,
                    EXAMTYPENAME,
                    QUESTIONTYPEPLUGINVERSION,
                    ANSWERTYPE,
                    ANSWERTYPEPLUGINVERSION,
                    PLUGINDATA
                FROM QUESTION
                WHERE QUESTIONID = ?"""

        val sqlSubQuestionQuery = """
            SELECT
                QUESTIONID,
                QUESTIONTYPE,
                QUESTIONTEXT,
                COURSEID,
                EXAMTYPENAME,
                QUESTIONTYPEPLUGINVERSION,
                ANSWERTYPE,
                ANSWERTYPEPLUGINVERSION,
                PLUGINDATA
            FROM QUESTION
            WHERE PARENTQUESTIONID = ?"""

        val questions: ArrayList<Question>
        try {
            preparedQuestionStatement = conn?.prepareStatement(sqlQuestionQuery)
            preparedQuestionStatement?.setInt(1, questionId)

            questions = initQuestionsByResultSet(preparedQuestionStatement, sqlSubQuestionQuery, conn)

        } catch (e: SQLException) {
            val message = "Question could not be retrieved from the database. ID: $questionId"
            logger.error(message, e)
            throw DatabaseException(message, e)
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedQuestionStatement)
        }

        if (questions.isEmpty()) throw DatabaseException("No questions found for $questionId")

        return questions.first()
    }

    /**
     * Updates a question.
     *
     * @param question [Question] Question that should be updated.
     * @return [Question] The updated question
     */
    override fun updateQuestion(question: Question): Question {
        val conn: Connection? = MySQLConnection.getConnection()
        var preparedStatement: PreparedStatement? = null

        val updateQuestionQuery = """
            UPDATE QUESTION
            SET EXAMTYPENAME = ?,
                COURSEID = ?,
                QUESTIONTEXT = ?,
                QUESTIONTYPE = ?,
                ANSWERTYPE = ?,
                ANSWERTYPEPLUGINVERSION = ?,
                PLUGINDATA = ?,
                QUESTIONTYPEPLUGINVERSION = ?
            WHERE QUESTIONID = ?"""

        val updatePartialAnswerQuery = """
           INSERT INTO PARTIAL_ANSWER (PARTIALANSWERID, QUESTIONID, PARTIALANSWERTEXT)
           VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE PARTIALANSWERTEXT = ?
        """

        try {
            preparedStatement = conn?.prepareStatement(updateQuestionQuery)
            preparedStatement?.setString(1, question.examType)
            preparedStatement?.setInt(2, question.courseId)
            preparedStatement?.setString(3, question.questionText)
            preparedStatement?.setString(4, question.questionType)
            preparedStatement?.setString(5, question.answerType)
            preparedStatement?.setString(6, question.answerTypePluginVersion)
            preparedStatement?.setString(7, question.pluginData)
            preparedStatement?.setString(8, question.questionTypePluginVersion)
            preparedStatement?.setInt(9, question.questionId ?: throw DatabaseException("No questionID provided"))
            preparedStatement?.executeUpdate()

            val preparedStatementPartialAnswer = conn?.prepareStatement(updatePartialAnswerQuery, Statement.RETURN_GENERATED_KEYS)
            val newPartialAnswerIds = ArrayDeque<Int>()
            question.partialAnswers.forEachIndexed { index, partialAnswer ->
                partialAnswer.id?.let {
                    preparedStatementPartialAnswer?.setInt(1, it)
                } ?: run {
                    newPartialAnswerIds.add(index)
                    preparedStatementPartialAnswer?.setNull(1, java.sql.Types.INTEGER)
                }

                preparedStatementPartialAnswer?.setInt(2, question.questionId
                        ?: throw DatabaseException("Can't update partial answer without question ID"))
                preparedStatementPartialAnswer?.setString(3, partialAnswer.text)
                preparedStatementPartialAnswer?.setString(4, partialAnswer.text)
                preparedStatementPartialAnswer?.addBatch()
            }
            preparedStatementPartialAnswer?.executeBatch()
                    ?: throw DatabaseException("Couldn't insert partial answer batch")

            preparedStatementPartialAnswer.generatedKeys.use {
                while (it.next() && !newPartialAnswerIds.isEmpty())
                    question.partialAnswers[newPartialAnswerIds.pop()].id = it.getInt(1)

            }

        } catch (e: SQLException) {
            logger.error("Something went wrong while updating questions", e)
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
        }
        return question
    }

    /**
     * Check if question is answered by students.
     *
     * @param questionIds [Array]<[Int]> The IDs of the questions
     * @return [Boolean] true if any of them have been answered, otherwise false
     */
    override fun answersGivenOnQuestions(questionIds: Array<Int>): Boolean {
        val conn: Connection? = MySQLConnection.getConnection()
        var preparedStatement: PreparedStatement? = null

        var query = "SELECT COUNT(*) AS N FROM GIVEN_ANSWER WHERE QUESTIONINEXAMID = ?"

        for (questionId in questionIds.copyOfRange(0, questionIds.size - 1))
            query += " OR QUESTIONINEXAMID = ?"

        var thereAreAnswersGivenToQuestions = false

        try {
            preparedStatement = conn?.prepareStatement(query)
            for ((i, questionId) in questionIds.withIndex())
                preparedStatement?.setInt(i + 1, questionId)


            val rs = preparedStatement?.executeQuery()
                    ?: throw DatabaseException("Couldn't execute statement")

            rs.next()
            val n = rs.getInt("N")
            if (n > 0)
                thereAreAnswersGivenToQuestions = true

        } catch (e: SQLException) {
            logger.error("Something went wrong while getting all courses", e)
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeConnection(conn)
            MySQLConnection.closeStatement(preparedStatement)
        }

        return thereAreAnswersGivenToQuestions
    }

    /**
     * Gets the questions that a student hasn't answered yet in a course.
     *
     * @param studentNr [Int] the studentNumber
     * @param courseId [Int] the ID of the course
     * @return [ArrayList]<[Question]> the questions
     */
    override fun getQuestionsNotAnsweredByStudentInCourse(studentNr: Int, courseId: Int): ArrayList<Question> {
        var conn: Connection? = null
        var preparedStatement: PreparedStatement? = null

        val query = """SELECT * FROM QUESTION"""
        return try {
            conn = MySQLConnection.getConnection()
            preparedStatement = conn?.prepareStatement(query)
            preparedStatement?.setInt(1, studentNr)
            preparedStatement?.setInt(2, courseId)
            val rs = preparedStatement?.executeQuery() ?: throw DatabaseException("Couldn't execute statement")
            val results = ArrayList<Question>()
            while (rs.next()) {
                val questionId = rs.getInt("QUESTIONID")
                results.add(Question(
                        courseId = rs.getInt("COURSEID"),
                        questionText = rs.getString("QUESTIONTEXT"),
                        questionType = rs.getString("QUESTIONTYPE"),
                        questionTypePluginVersion = rs.getString("QUESTIONTYPEPLUGINVERSION"),
                        categories = getCategoriesOfQuestion(questionId, conn),
                        answerType = rs.getString("ANSWERTYPE"),
                        answerTypePluginVersion = rs.getString("ANSWERTYPEPLUGINVERSION"),
                        examType = rs.getString("EXAMTYPENAME"),
                        pluginData = rs.getString("PLUGINDATA"),
                        partialAnswers = getPartialAnswers(conn, questionId),
                        questionId = questionId
                ))
            }
            results
        } catch (e: SQLException) {
            val message = "Something went wrong while getting questions"
            logger.error(message, e)
            throw DatabaseException(message, e)
        } finally {
            MySQLConnection.closeConnection(conn)
            preparedStatement?.close()
        }
    }
}
