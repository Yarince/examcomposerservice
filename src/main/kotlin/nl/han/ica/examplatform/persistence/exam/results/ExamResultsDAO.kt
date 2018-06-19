package nl.han.ica.examplatform.persistence.exam.results

import nl.han.ica.examplatform.models.exam.PracticeExamResult
import nl.han.ica.examplatform.models.question.QuestionResult
import nl.han.ica.examplatform.models.question.QuestionResultStats
import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.DatabaseException
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * DAO for retrieving exam results.
 */
@Repository
class ExamResultsDAO : IExamResultsDAO {
    private val logger = loggerFor(javaClass)

    /**
     * Gets the previous results of a student in a course.
     *
     * @param studentNr [Int] the student nr.
     * @param courseId [Int] the courseID
     * @return [ArrayList]<[PracticeExamResult]>
     */
    override fun getPreviousResultsOfStudent(studentNr: Int, courseId: Int): ArrayList<PracticeExamResult> {
        var dbConnection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        val getResultsQuery = """SELECT SUBMITTEDEXAMID, Q.QUESTIONID, CATEGORYNAME, QUESTIONTEXT, QUESTIONTYPE, RESULT
            FROM QUESTION Q
            INNER JOIN PRACTICETEST_QUESTION_RESULT P ON Q.QUESTIONID = P.QUESTIONID
            INNER JOIN CATEGORIES_OF_QUESTION COQ ON COQ.QUESTIONID = Q.QUESTIONID
            INNER JOIN CATEGORY C ON COQ.CATEGORYID = C.CATEGORYID
            WHERE COURSEID = ? AND STUDENTNUMBER = ?"""
        return try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(getResultsQuery)
            preparedStatement?.setInt(1, courseId)
            preparedStatement?.setInt(2, studentNr)
            val rs = preparedStatement?.executeQuery() ?: throw DatabaseException("Couldn't execute statement")
            val results = ArrayList<PracticeExamResult>()
            while (rs.next()) {
                val examId = rs.getInt("SUBMITTEDEXAMID")
                val questionId = rs.getInt("QUESTIONID")
                val questionResult = QuestionResult(questionId,
                        categories = arrayListOf(rs.getString("CATEGORYNAME")),
                        questionText = rs.getString("QUESTIONTEXT"),
                        type = rs.getString("QUESTIONTYPE"),
                        submittedExamId = examId,
                        wasCorrect = rs.getBoolean("RESULT"))
                val result = results.find { it.examId == examId }
                if (result == null) {
                    results.add(PracticeExamResult(examId, studentNr, arrayListOf(questionResult)))
                } else {
                    val question = result.questions.find { it.questionId == questionId }
                    if (question == null) {
                        result.questions.add(questionResult)
                    } else {
                        question.categories.add(rs.getString("CATEGORYNAME"))
                        result.questions[result.questions.indexOf(question)] = question
                    }
                    results[results.indexOf(result)] = result
                }
            }
            results
        } catch (e: SQLException) {
            val message = "Something went wrong while getting results of student"
            logger.error(message, e)
            throw DatabaseException(message, e)
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            preparedStatement?.close()
        }
    }

    /**
     * Gets the combined results of other students in a specific category.
     *
     * @param studentNr [Int] the student nr.
     * @param category [String] the category
     * @return [ArrayList]<[QuestionResultStats]>
     */
    override fun getResultsOfOthersInCategory(studentNr: Int, category: String): ArrayList<QuestionResultStats> {
        var dbConnection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        val getResultsOfOthersQuery = """SELECT PQR.QUESTIONID, COUNT(PQR.QUESTIONID) AS NRESULTS, (SELECT COUNT(PQR.RESULT) FROM PRACTICETEST_QUESTION_RESULT PQR WHERE PQR.RESULT=TRUE AND EXISTS(SELECT 1
            FROM QUESTION Q
            WHERE PQR.QUESTIONID = Q.QUESTIONID AND EXISTS(
                                                            SELECT 1
                                                            FROM CATEGORIES_OF_QUESTION COQ
                                                            WHERE COQ.QUESTIONID = Q.QUESTIONID AND EXISTS(
                                                                                                            SELECT 1
                                                                                                            FROM CATEGORY C
                                                                                                            WHERE C.CATEGORYID = COQ.CATEGORYID AND C.CATEGORYNAME = ?
                                                                                                            )
                                                            )
            ) ) AS NCORRECT
            FROM PRACTICETEST_QUESTION_RESULT PQR
            WHERE EXISTS(
            SELECT 1
            FROM QUESTION Q
            WHERE PQR.QUESTIONID = Q.QUESTIONID AND EXISTS(
                                                            SELECT 1
                                                            FROM CATEGORIES_OF_QUESTION COQ
                                                            WHERE COQ.QUESTIONID = Q.QUESTIONID AND EXISTS(
                                                                                                            SELECT 1
                                                                                                            FROM CATEGORY C
                                                                                                            WHERE C.CATEGORYID = COQ.CATEGORYID AND C.CATEGORYNAME = ?
                                                                                                            )
                                                            )
            )
            GROUP BY PQR.QUESTIONID
        """
        return try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(getResultsOfOthersQuery)
            preparedStatement?.setString(1, category)
            preparedStatement?.setString(2, category)
            val rs = preparedStatement?.executeQuery() ?: throw DatabaseException("Couldn't execute statement")
            val results = ArrayList<QuestionResultStats>()
            while (rs.next()) {
                val nCorrect = rs.getInt("NCORRECT")
                val nTotal = rs.getInt("NRESULTS")
                results.add(QuestionResultStats(
                        rs.getInt("QUESTIONID"),
                        nTotal,
                        nCorrect,
                        nTotal - nCorrect))
            }
            results
        } catch (e: SQLException) {
            val message = "Something went wrong while getting results of other students"
            logger.error(message, e)
            throw DatabaseException(message, e)
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            preparedStatement?.close()
        }
    }
}