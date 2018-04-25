package nl.han.ica.examplatform.persistence.exam

import nl.han.ica.examplatform.controllers.responseexceptions.DatabaseException
import nl.han.ica.examplatform.controllers.responseexceptions.ExamNotFoundException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.models.question.QuestionType
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.*


@Repository
class ExamDAO {

    fun getExams(): ArrayList<SimpleExam> {
        val dbConnection: Connection? = MySQLConnection.getConnection()
        val sqlQueryStringInsertString = "SELECT EXAMID, EXAMNAME, COURSECODE FROM EXAM INNER JOIN COURSE ON EXAM.COURSEID = COURSE.COURSEID"
        val preparedStatement: PreparedStatement?
        preparedStatement = dbConnection?.prepareStatement(sqlQueryStringInsertString)

        val result = ArrayList<SimpleExam>()
        try {
            val resultSet = preparedStatement?.executeQuery()

            while (resultSet!!.next()) {
                result.add(SimpleExam(resultSet.getInt("ExamID"),
                        resultSet.getString("ExamName"),
                        resultSet.getString("CourseCode")))
            }

        } catch (e: SQLException) {
            e.printStackTrace()
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
            MySQLConnection.closeConnection(dbConnection)
        }

        return result
    }

    fun getExam(id: Int): Exam {
        val dbConnection: Connection? = MySQLConnection.getConnection()
        val sqlQueryStringInsertString = "SELECT * FROM EXAM WHERE EXAMID = $id"
        val preparedStatement: PreparedStatement?
        preparedStatement = dbConnection?.prepareStatement(sqlQueryStringInsertString)
        val result: Exam

        try {
            val rs = preparedStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")

            rs.last()
            if (rs.row < 1)
                throw ExamNotFoundException("Exam with ID $id was not found")

            result = Exam(examId = rs.getInt("ExamID"),
                    durationInMinutes = 999,
                    startTime = rs.getDate("StartTime"),
                    course = "APP",
                    examType = ExamType.EXAM,
                    name = rs.getString("ExamName"),
                    location = rs.getString("Location"),
                    instructions = rs.getString("Instructions"),
                    questions = arrayOf(Question(questionId = 1,
                            questionType = QuestionType.OPEN_QUESTION,
                            questionText = "Explain why DCAR is better",
                            examType = ExamType.EXAM), Question(questionId = 2,
                            questionType = QuestionType.MULTIPLE_CHOICE_QUESTION,
                            questionText = "Choose between A, B and C",
                            examType = ExamType.EXAM)
                    )
            )
        } catch (e: SQLException) {
            e.printStackTrace()
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
            MySQLConnection.closeConnection(dbConnection)
        }

        return result
    }
}