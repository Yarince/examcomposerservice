package nl.han.ica.examplatform.persistence.exam

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.DatabaseException
import nl.han.ica.examplatform.controllers.exam.ExamNotFoundException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.Date
import kotlin.collections.ArrayList

/**
 * This class handles all the Database operations for [Exam].
 */
@Repository
class ExamDAO : IExamDAO {

    private val logger = loggerFor(javaClass)

    /**
     * This function gets a list of minimized Exams.
     *
     * @return [ArrayList]<[SimpleExam]> a list of SimpleExams retrieved from the database.
     */
    override fun getExams(): ArrayList<SimpleExam> {
        val conn: Connection? = MySQLConnection.getConnection()
        val examQuery = """
            SELECT
                EXAMID,
                EXAMNAME,
                EXAM.COURSEID
            FROM EXAM
                INNER JOIN COURSE ON EXAM.COURSEID = COURSE.COURSEID
        """.trimIndent()
        val preparedStatement: PreparedStatement?
        preparedStatement = conn?.prepareStatement(examQuery)

        val result = ArrayList<SimpleExam>()
        try {
            val resultSet = preparedStatement?.executeQuery()
                    ?: throw DatabaseException("Prepared statement couldn't be made")

            while (resultSet.next())
                result.add(SimpleExam(resultSet.getInt("ExamID"),
                        resultSet.getString("ExamName"),
                        resultSet.getInt("COURSEID")))
        } catch (e: SQLException) {
            logger.error("Error when retrieving exams", e)
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
            MySQLConnection.closeConnection(conn)
        }

        return result
    }

    /**
     * Gets all information about an Exam.
     *
     * @param id [Int] The ID of which all information should be queried
     * @return [Exam] The Exam added to the database.
     */
    override fun getExam(id: Int): Exam {
        val conn: Connection? = MySQLConnection.getConnection()

        val examQuery = """
            SELECT
                EXAMID,
                STARTTIME,
                ENDTIME,
                EXAM.COURSEID,
                EXAM.EXAMTYPENAME,
                EXAMNAME,
                LOCATION,
                INSTRUCTIONS,
                READYFORDOWNLOAD,
                DECRYPTKEY
            FROM EXAM
                INNER JOIN COURSE ON EXAM.COURSEID = COURSE.COURSEID
            WHERE EXAMID = ?
           """
        val examStatement: PreparedStatement?
        examStatement = conn?.prepareStatement(examQuery)
        examStatement?.setInt(1, id)

        val result: Exam

        try {
            val examRs = examStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")

            // Move to the last result, so we can use getRow on ResultSet
            examRs.last()

            // This is the row of of the last result, so if this is smaller than 0
            if (examRs.row < 1) throw ExamNotFoundException("Exam with ID $id was not found")

            result = Exam(examId = examRs.getInt("ExamID"),
                    durationInMinutes = ((examRs.getTime("EndTime").time / 60000) -
                            (examRs.getTime("StartTime").time / 60000)).toInt(),
                    startTime = Date(examRs.getTimestamp("StartTime").time),
                    endTime = Date(examRs.getTimestamp("EndTime").time),
                    courseId = examRs.getInt("CourseID"),
                    examType = examRs.getString("EXAMTYPENAME"),
                    name = examRs.getString("ExamName"),
                    location = examRs.getString("Location"),
                    instructions = examRs.getString("Instructions"),
                    questions = null,
                    readyForDownload = examRs.getBoolean("READYFORDOWNLOAD"),
                    decryptionCodes = examRs.getString("DECRYPTKEY") ?: "Geen decryptiecode ingesteld"
            )
        } catch (e: SQLException) {
            logger.error("Error while getting exam $id", e)
            throw DatabaseException("Error while interacting with the database", e)
        } finally {
            MySQLConnection.closeStatement(examStatement)
            MySQLConnection.closeConnection(conn)
        }

        return result
    }

    /**
     * Inserts an Exam into the database.
     *
     * @param exam [Exam] The Exam that should be inserted
     * @return [Exam] The Exam added to the database
     */
    override fun insertExam(exam: Exam): Exam {
        var examToReturn = exam

        val conn: Connection? = MySQLConnection.getConnection()
        val insertExamQuery = """
            INSERT INTO EXAM (
                COURSEID,
                EXAMTYPENAME,
                EXAMCODE,
                EXAMNAME,
                STARTTIME,
                ENDTIME,
                INSTRUCTIONS,
                EXAMVERSION,
                LOCATION,
                READYFORDOWNLOAD)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()
        val preparedStatement: PreparedStatement?
        preparedStatement = conn?.prepareStatement(insertExamQuery)
        preparedStatement?.setInt(1, exam.courseId)
        preparedStatement?.setString(2, exam.examType)
        preparedStatement?.setString(3, exam.name)
        preparedStatement?.setString(4, exam.name)
        preparedStatement?.setDate(5, java.sql.Date(exam.startTime.time))
        preparedStatement?.setDate(6, java.sql.Date(exam.endTime.time))
        preparedStatement?.setString(7, exam.instructions)
        preparedStatement?.setInt(8, exam.version)
        preparedStatement?.setString(9, exam.location)
        preparedStatement?.setBoolean(10, exam.readyForDownload ?: false)

        try {
            val insertedRows = preparedStatement?.executeUpdate()
            if (insertedRows == 1) {
                val idQuery = "SELECT LAST_INSERT_ID() AS ID"
                val idPreparedStatement = conn?.prepareStatement(idQuery)
                val result = idPreparedStatement?.executeQuery()
                result?.let {
                    while (result.next())
                        examToReturn = examToReturn.copy(examId = result.getInt("ID"))
                }
            }
        } catch (e: SQLException) {
            logger.error("Error while inserting exam in database", e)
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
            MySQLConnection.closeConnection(conn)
        }

        return examToReturn
    }

    /**
     * Adds single or multiple questions to an exam.
     *
     * @param exam [Exam] The containing all [Question]s
     * @return [Exam] The updated Exam that was given.
     */
    override fun addQuestionsToExam(exam: Exam): Exam {
        if (exam.questions == null || exam.questions.size < 1)
            throw DatabaseException("Please provide questions to add to exam")
        if (exam.examId == null)
            throw DatabaseException("Please provide examID")

        val sqlQuestionQuery = """
            INSERT INTO QUESTION_IN_EXAM (
                EXAMID,
                QUESTIONID,
                SEQUENCENUMBER,
                QUESTIONPOINTS)
            VALUES (?, ?, ?, ?)
        """

        val sqlPartialAnswerQuery = """
           INSERT INTO PARTIAL_ANSWER_IN_QUESTION_IN_EXAM (
               EXAMID,
               QUESTIONID,
               PARTIALANSWERID,
               POINTS
               )
           VALUES (?, ?, ?, ?)
        """

        val conn: Connection? = MySQLConnection.getConnection()
        val preparedStatementQuestion: PreparedStatement? = conn?.prepareStatement(sqlQuestionQuery)

        try {
            for (question in exam.questions) {
                preparedStatementQuestion?.setInt(1, exam.examId)
                preparedStatementQuestion?.setInt(2, question.questionId
                        ?: throw DatabaseException("Can't insert question without ID"))
                preparedStatementQuestion?.setInt(3, question.questionOrderInExam
                        ?: throw DatabaseException("Can't insert question without sequence number"))
                preparedStatementQuestion?.setInt(4, question.questionPoints
                        ?: throw DatabaseException("Can't insert question without question points"))
                preparedStatementQuestion?.addBatch()

                val preparedStatementPartialAnswer = conn?.prepareStatement(sqlPartialAnswerQuery)
                for (partialAnswer in question.partialAnswers) {
                    preparedStatementPartialAnswer?.setInt(1, exam.examId)
                    preparedStatementPartialAnswer?.setInt(2, question.questionId
                            ?: throw DatabaseException("Can't insert partial answer without question ID"))
                    preparedStatementPartialAnswer?.setInt(3, partialAnswer.id
                            ?: throw DatabaseException("Can't insert partial answer without ID"))
                    preparedStatementPartialAnswer?.setInt(4, partialAnswer.points
                            ?: throw DatabaseException("Can't insert partial answer without points"))
                    preparedStatementPartialAnswer?.addBatch()
                }
                preparedStatementPartialAnswer?.executeBatch()
            }

            preparedStatementQuestion?.executeBatch()
        } catch (e: SQLException) {
            logger.error("Error when adding questions to exam", e)
            throw DatabaseException("Error while interacting with the database", e)
        } finally {
            MySQLConnection.closeStatement(preparedStatementQuestion)
            MySQLConnection.closeConnection(conn)
        }

        return exam
    }

    override fun addClassesToExam(examId: Int, classes: ArrayList<String>): HttpStatus {
        if (classes.isEmpty())
            throw DatabaseException("Please provide a class to add to this exam")
        val conn: Connection? = MySQLConnection.getConnection()
        val sqlAddClassesToExamQuery = "INSERT INTO CLASSES_TAKING_EXAMS (CLASSNAME, EXAMID) VALUES(?, ?)"

        val preparedStatement: PreparedStatement? = conn?.prepareStatement(sqlAddClassesToExamQuery)
        return try {
            for (`class` in classes.withIndex()) {
                preparedStatement?.setString(1, `class`.value)
                preparedStatement?.setInt(2, examId)

                preparedStatement?.executeUpdate()
            }
            HttpStatus.ACCEPTED
        } catch (e: SQLException) {
            logger.error("Error when adding questions to exam", e)
            throw DatabaseException("Error while interacting with the database")
        }
    }

    /**
     * Updates the meta data of an exam.
     *
     * @param exam [Exam] The Exam to update
     * @return [Exam] The updated exam
     */
    override fun updateExam(exam: Exam): Exam {
        if (exam.examId == null) throw DatabaseException("Can't update exam when examId is not set")

        val sqlExamQuery = """
            UPDATE EXAM
            SET
                COURSEID = ?,
                EXAMTYPENAME = ?,
                EXAMNAME = ?,
                STARTTIME = ?,
                ENDTIME = ?,
                INSTRUCTIONS = ?,
                EXAMVERSION = ?,
                LOCATION = ?
            WHERE EXAMID = ?
            """
        val sqlQuestionQuery = """
            INSERT INTO QUESTION_IN_EXAM (EXAMID, QUESTIONID, SEQUENCENUMBER, QUESTIONPOINTS)
            VALUE (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE SEQUENCENUMBER = ?, QUESTIONPOINTS = ?
        """

        val sqlPartialAnswerQuery = """
           INSERT INTO PARTIAL_ANSWER_IN_QUESTION_IN_EXAM (EXAMID, QUESTIONID, PARTIALANSWERID, POINTS)
           VALUE (?, ?, ?, ?)
           ON DUPLICATE KEY UPDATE POINTS = ?
        """

        val conn: Connection? = MySQLConnection.getConnection()
        val preparedStatementExam: PreparedStatement?
        preparedStatementExam = conn?.prepareStatement(sqlExamQuery)

        preparedStatementExam?.setInt(1, exam.courseId)
        preparedStatementExam?.setString(2, exam.examType)
        preparedStatementExam?.setString(3, exam.name)
        preparedStatementExam?.setDate(4, java.sql.Date(exam.startTime.time))
        preparedStatementExam?.setDate(5, java.sql.Date(exam.endTime.time))
        preparedStatementExam?.setString(6, exam.instructions)
        preparedStatementExam?.setInt(7, exam.version)
        preparedStatementExam?.setString(8, exam.location)
        preparedStatementExam?.setInt(9, exam.examId)
        preparedStatementExam?.executeUpdate()
        try {
            if (exam.questions != null) {
                for (question in exam.questions) {
                    // Insert or update question
                    val preparedStatementQuestion: PreparedStatement? = conn?.prepareStatement(sqlQuestionQuery)
                    preparedStatementQuestion?.setInt(1, exam.examId)
                    preparedStatementQuestion?.setInt(2, question.questionId
                            ?: throw DatabaseException("Can't update question without ID"))
                    preparedStatementQuestion?.setInt(3, question.questionOrderInExam
                            ?: throw DatabaseException("Can't update question without sequence number"))
                    preparedStatementQuestion?.setInt(4, question.questionPoints
                            ?: throw DatabaseException("Can't update question without question points"))
                    preparedStatementQuestion?.setInt(5, question.questionOrderInExam
                            ?: throw DatabaseException("Can't update question without sequence number"))
                    preparedStatementQuestion?.setInt(6, question.questionPoints
                            ?: throw DatabaseException("Can't update question without question points"))
                    preparedStatementQuestion?.execute()

                    val preparedStatementPartialAnswer = conn?.prepareStatement(sqlPartialAnswerQuery)
                    for (partialAnswer in question.partialAnswers) {
                        preparedStatementPartialAnswer?.setInt(1, exam.examId)
                        preparedStatementPartialAnswer?.setInt(2, question.questionId
                                ?: throw DatabaseException("Can't update partial answer without question ID"))
                        preparedStatementPartialAnswer?.setInt(3, partialAnswer.id
                                ?: throw DatabaseException("Can't update partial answer without ID"))
                        preparedStatementPartialAnswer?.setInt(4, partialAnswer.points
                                ?: throw DatabaseException("Can't update partial answer without points"))
                        preparedStatementPartialAnswer?.setInt(5, partialAnswer.points
                                ?: throw DatabaseException("Can't update partial answer without points"))
                        preparedStatementPartialAnswer?.addBatch()
                    }
                    preparedStatementPartialAnswer?.executeBatch()
                }
            }
        } catch (e: SQLException) {
            logger.error("Error while publishing exam", e)
            throw DatabaseException("Error while updating exam", e)
        } finally {
            MySQLConnection.closeStatement(preparedStatementExam)
            MySQLConnection.closeConnection(conn)
        }
        return exam
    }

    /**
     * Changes the order of questions in an exam
     *
     * @param examId [Int] The ID of the exam
     * @param questionsAndSequenceNumbers [Array]<[Pair]<[Int], [Int]>> An array containing the questionIds and the new sequence number
     */
    override fun changeQuestionOrderInExam(examId: Int, questionsAndSequenceNumbers: Array<Pair<Int, Int>>) {
        if (questionsAndSequenceNumbers.size < 2)
            throw DatabaseException("Can't change order if no or only 1 questions and sequencenumbers are provided")

        val conn: Connection? = MySQLConnection.getConnection()
        var preparedStatement: PreparedStatement? = null

        try {
            val query = "UPDATE QUESTION_IN_EXAM SET SEQUENCENUMBER = ? WHERE QUESTIONID = ? AND EXAMID = ?"

            for (pair in questionsAndSequenceNumbers) {
                preparedStatement = conn?.prepareStatement(query)
                preparedStatement?.setInt(1, pair.second)
                preparedStatement?.setInt(2, pair.first)
                preparedStatement?.setInt(3, examId)
                preparedStatement?.executeUpdate()
            }
        } catch (e: SQLException) {
            logger.error("Error while publishing exam", e)
            throw DatabaseException("Error while updating exam")
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
            MySQLConnection.closeConnection(conn)
        }
    }

    /**
     * Publishes an exam.
     *
     * @param examId [Int] The ID of the exam that should be published
     */
    override fun publishExam(examId: Int, shouldBePublished: Boolean) {
        val query = "UPDATE EXAM SET READYFORDOWNLOAD = ? WHERE EXAMID = ?"

        val conn: Connection? = MySQLConnection.getConnection()
        val preparedStatement: PreparedStatement?
        preparedStatement = conn?.prepareStatement(query)

        preparedStatement?.setBoolean(1, shouldBePublished)
        preparedStatement?.setInt(2, examId)

        try {
            preparedStatement?.executeUpdate()
        } catch (e: SQLException) {
            logger.error("Error while publishing exam $examId", e)
            throw DatabaseException("Error while publishing exam $examId")
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
            MySQLConnection.closeConnection(conn)
        }
    }

    /**
     * De-couples questions from an exam.
     *
     * @param examId [Int] The ID of the exam
     * @param questionIds [Array]<[Int]> Array containing the IDs of the questions that should be removed
     */
    override fun removeQuestionsFromExam(examId: Int, questionIds: Array<Int>) {
        var query = "DELETE FROM QUESTION_IN_EXAM WHERE EXAMID = ? AND QUESTIONID = ?"

        val conn: Connection? = MySQLConnection.getConnection()
        val preparedStatement: PreparedStatement?

        for (questionId in questionIds.copyOfRange(0, questionIds.size - 1)) {
            query += " OR QUESTIONID = ?"
        }

        preparedStatement = conn?.prepareStatement(query)

        try {
            preparedStatement?.setInt(1, examId)
            for ((i, questionId) in questionIds.withIndex()) {
                preparedStatement?.setInt(i + 2, questionId)
            }

            preparedStatement?.executeUpdate()
        } catch (e: SQLException) {
            logger.error("Error while publishing exam $examId", e)
            throw DatabaseException("Error while publishing exam $examId")
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
            MySQLConnection.closeConnection(conn)
        }
    }

    /**
     * Deletes an exam.
     * This doesn't delete any questions.
     *
     * @param examId [Int] The ID of the exam that should be deleted
     */
    override fun deleteExam(examId: Int) {
        val queries = arrayOf("DELETE FROM COMMENT_ON_QUESTION WHERE EXAMID = ?",
                "DELETE FROM PARTIAL_ANSWER_IN_QUESTION_IN_EXAM WHERE EXAMID = ?",
                "DELETE FROM GIVEN_ANSWER WHERE EXAMID = ?",
                "DELETE FROM QUESTION_IN_EXAM WHERE EXAMID = ?",
                "DELETE FROM EXAM WHERE EXAMID = ?"
        )

        val conn: Connection? = MySQLConnection.getConnection()

        try {
            for (query in queries) {
                val preparedStatementExam = conn?.prepareStatement(query)
                preparedStatementExam?.setInt(1, examId)
                preparedStatementExam?.executeUpdate()
                MySQLConnection.closeStatement(preparedStatementExam)
            }
        } catch (e: SQLException) {
            logger.error("Error deleting exam $examId", e)
            throw DatabaseException("Error while deleting exam $examId")
        } finally {
            MySQLConnection.closeConnection(conn)
        }
    }
}
