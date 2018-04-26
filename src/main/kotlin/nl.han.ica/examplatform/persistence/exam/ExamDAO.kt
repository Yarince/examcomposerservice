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
import kotlin.collections.ArrayList


@Repository
class ExamDAO {

    fun getExams(): ArrayList<SimpleExam> {
        val conn: Connection? = MySQLConnection.getConnection()
        val examQuery = "SELECT EXAMID, EXAMNAME, COURSECODE FROM EXAM INNER JOIN COURSE ON EXAM.COURSEID = COURSE.COURSEID"
        val preparedStatement: PreparedStatement?
        preparedStatement = conn?.prepareStatement(examQuery)

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
            MySQLConnection.closeConnection(conn)
        }

        return result
    }

    fun getExam(id: Int): Exam {
        val conn: Connection? = MySQLConnection.getConnection()

        val examQuery = "SELECT EXAMID, STARTTIME, ENDTIME, COURSECODE, EXAM.EXAMTYPEID, EXAMNAME, LOCATION, INSTRUCTIONS FROM EXAM INNER JOIN COURSE ON EXAM.COURSEID = COURSE.COURSEID INNER JOIN EXAMTYPE ON EXAM.EXAMTYPEID = EXAMTYPE.EXAMTYPEID WHERE EXAMID = ?"
        val examStatement: PreparedStatement?
        examStatement = conn?.prepareStatement(examQuery)
        examStatement?.setInt(1, id)

        val questionsQuery = "SELECT * FROM QUESTION INNER JOIN QUESTION_IN_EXAM ON QUESTION.QUESTIONID = QUESTION_IN_EXAM.QUESTIONID INNER JOIN COURSE ON QUESTION.COURSEID = COURSE.COURSEID WHERE QUESTION_IN_EXAM.EXAMID = ?"
        val questionsStatement: PreparedStatement?
        questionsStatement = conn?.prepareStatement(questionsQuery)
        questionsStatement?.setInt(1, id)

        val result: Exam

        try {
            val questionRs = questionsStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")
            val questions = ArrayList<Question>()
            while (questionRs.next()) {
                questions.add(Question(questionId = questionRs.getInt("QuestionID"),
                        questionText = questionRs.getString("QuestionText"),
                        questionType = QuestionType.from(questionRs.getString("QuestionType")),
                        course = questionRs.getString("CourseCode"),
                        examType = ExamType.from(questionRs.getInt("ExamTypeId"))))
            }
            val examRs = examStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")

            // Move to the last result, so we can use getRow on ResultSet
            examRs.last()

            // This is the row of of the last result, so if this is smaller than 0
            if (examRs.row < 1) throw ExamNotFoundException("Exam with ID $id was not found")

            result = Exam(examId = examRs.getInt("ExamID"),
                    durationInMinutes = ((examRs.getTime("EndTime").time / 60000) - (examRs.getTime("StartTime").time / 60000)).toInt(),
                    startTime = Date(examRs.getTimestamp("StartTime").time),
                    endTime = Date(examRs.getTimestamp("EndTime").time),
                    course = examRs.getString("CourseCode"),
                    examType = ExamType.from(examRs.getInt("ExamTypeId")),
                    name = examRs.getString("ExamName"),
                    location = examRs.getString("Location"),
                    instructions = examRs.getString("Instructions"),
                    questions = questions
            )
        } catch (e: SQLException) {
            e.printStackTrace()
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeStatement(examStatement)
            MySQLConnection.closeConnection(conn)
        }

        return result
    }

    fun insertExam(exam: Exam): Exam {
        val conn: Connection? = MySQLConnection.getConnection()
        val insertExamQuery = "INSERT INTO EXAM (COURSEID, EXAMTYPEID, EXAMCODE, EXAMNAME, STARTTIME, ENDTIME, INSTRUCTIONS, VERSION, LOCATION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        val preparedStatement: PreparedStatement?
        preparedStatement = conn?.prepareStatement(insertExamQuery)
        preparedStatement?.setInt(1, exam.courseId.value)
        preparedStatement?.setInt(2, exam.examType.examId)
        preparedStatement?.setString(3, exam.name)//TODO BRUH
        preparedStatement?.setString(4, exam.name)
        preparedStatement?.setDate(5, java.sql.Date(exam.startTime.time))
        preparedStatement?.setDate(6, java.sql.Date(exam.endTime.time))
        preparedStatement?.setString(7, exam.instructions)
        preparedStatement?.setInt(8, exam.version)
        preparedStatement?.setString(9, exam.location)

        try {
            preparedStatement?.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
            MySQLConnection.closeConnection(conn)
        }

        return exam
    }

    fun addQuestionsToExam(exam: Exam): Exam {
        if (exam.questions == null) throw DatabaseException("Please provide questions to add to exam")

        val query = "INSERT INTO QUESTION_IN_EXAM (EXAMID, QUESTIONID) VALUES "
        for (question in exam.questions) {
            query.plus("(?, ?)")
            if (question != exam.questions.last()) query.plus(", ")
        }


        val conn: Connection? = MySQLConnection.getConnection()
        val preparedStatement: PreparedStatement?
        preparedStatement = conn?.prepareStatement(query)

        var index = 0
        for (question in exam.questions) {
            preparedStatement?.setInt(index++, exam.examId ?: throw DatabaseException("Please provide examID"))
            preparedStatement?.setInt(index++, question.questionId ?: throw DatabaseException("Can't insert question without ID"))
        }

        try {
            preparedStatement?.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
            MySQLConnection.closeConnection(conn)
        }

        return exam
    }
}