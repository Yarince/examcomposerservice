package nl.han.ica.examplatform.persistence.exam.results

import nl.han.ica.examplatform.business.exam.practice.Results
import nl.han.ica.examplatform.business.exam.practice.models.QuestionResult
import nl.han.ica.examplatform.business.exam.practice.models.QuestionResultStats
import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.DatabaseException
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

@Repository
class ExamResultsDAO : IExamResultsDAO {
    private val logger = loggerFor(javaClass)

    override fun getPreviousResultsOfStudent(studentNr: Int, courseId: Int): ArrayList<Results> {
        var dbConnection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        val query = """SELECT PRACTICETESTRESULTID, Q.QUESTIONID, CATEGORYNAME, QUESTIONTEXT, QUESTIONTYPE, RESULT
            FROM QUESTION Q INNER JOIN PRACTICETEST_QUESTION_RESULT P
            ON Q.QUESTIONID = P.QUESTIONID INNER JOIN CATEGORIES_OF_QUESTION COQ
            ON COQ.QUESTIONID = Q.QUESTIONID INNER JOIN CATEGORY C ON
            COQ.CATEGORYID = C.CATEGORYID WHERE COURSEID = ? AND STUDENTNUMBER = ?"""
        return try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(query)
            preparedStatement?.setInt(1, courseId)
            preparedStatement?.setInt(2, studentNr)
            val rs = preparedStatement?.executeQuery() ?: throw DatabaseException("Couldn't execute statement")
            val results = ArrayList<Results>()
            while (rs.next()) {
                val examId = rs.getInt("PRACTICETESTRESULTID")
                val questionId = rs.getInt("QUESTIONID")
                val questionResult = QuestionResult(questionId,
                        categories = arrayListOf(rs.getString("CATEGORYNAME")),
                        questionText = rs.getString("QUESTIONTEXT"),
                        type = rs.getString("QUESTIONTYPE"),
                        practiceTestResultId = examId,
                        wasCorrect = rs.getBoolean("RESULT"))
                val result = results.find { it.examId == examId }
                if (result == null) {
                    results.add(Results(examId, studentNr, arrayListOf(questionResult)))
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

    override fun getResultsOfOthersInCategory(studentNr: Int, category: String): ArrayList<QuestionResultStats> {
        var dbConnection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        val query = "SELECT QUESTIONTEXT FROM QUESTION WHERE COURSEID = ?"
        return try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(query)
            preparedStatement?.setInt(1, studentNr)
            preparedStatement?.setString(2, category)
            val rs = preparedStatement?.executeQuery() ?: throw DatabaseException("Couldn't execute statement")
            val results = ArrayList<QuestionResultStats>()
            while (rs.next()) {
                results.add(QuestionResultStats(
                        rs.getInt("QUESTIONID"),
                        rs.getInt("NRESULTS"),
                        rs.getInt("NCORRECT"),
                        rs.getInt("NWRONG")))
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

    override fun getQuestionsAnsweredByStudentInCourse(studentNr: Int, courseId: Int): ArrayList<QuestionResult> {

    }

}