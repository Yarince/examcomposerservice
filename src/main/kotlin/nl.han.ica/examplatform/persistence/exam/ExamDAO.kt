package nl.han.ica.examplatform.persistence.exam

import nl.han.ica.examplatform.controllers.responseexceptions.DatabaseException
import nl.han.ica.examplatform.controllers.responseexceptions.ExamNotFoundException
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.exam.OfficialExam
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

/**
 * This class handles all the Database operations for [OfficialExam]
 */
@Repository
class ExamDAO {

    fun generatePracticeExam() : OfficialExam? {
        return null
    }

    /**
     * This function gets a list of minimized exams
     *
     * @return [ArrayList] a list of [SimpleExam]
     */
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

    /**
     * Gets all information about an exam
     *
     * @param id [Int] The ID of which all information should be queried
     * @return returns the [OfficialExam]
     */
    fun getExam(id: Int): OfficialExam {
        val conn: Connection? = MySQLConnection.getConnection()

        val examQuery = "SELECT EXAMID, STARTTIME, ENDTIME, EXAM.COURSEID, EXAM.EXAMTYPEID, EXAMNAME, LOCATION, INSTRUCTIONS FROM EXAM INNER JOIN COURSE ON EXAM.COURSEID = COURSE.COURSEID INNER JOIN EXAMTYPE ON EXAM.EXAMTYPEID = EXAMTYPE.EXAMTYPEID WHERE EXAMID = ?"
        val examStatement: PreparedStatement?
        examStatement = conn?.prepareStatement(examQuery)
        examStatement?.setInt(1, id)

        val questionsQuery = "SELECT * FROM QUESTION INNER JOIN QUESTION_IN_EXAM ON QUESTION.QUESTIONID = QUESTION_IN_EXAM.QUESTIONID INNER JOIN COURSE ON QUESTION.COURSEID = COURSE.COURSEID WHERE QUESTION_IN_EXAM.EXAMID = ?"
        val questionsStatement: PreparedStatement?
        questionsStatement = conn?.prepareStatement(questionsQuery)
        questionsStatement?.setInt(1, id)

        val result: OfficialExam

        try {
            val questionRs = questionsStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")
            val questions = ArrayList<Question>()
            while (questionRs.next()) {
                questions.add(Question(questionId = questionRs.getInt("QuestionID"),
                        questionText = questionRs.getString("QuestionText"),
                        questionType = QuestionType.from(questionRs.getString("QuestionType")),
                        courseId = questionRs.getInt("CourseID"),
                        examTypeId = ExamType.from(questionRs.getInt("ExamTypeId"))))
            }
            val examRs = examStatement?.executeQuery()
                    ?: throw DatabaseException("Error while interacting with the database")

            // Move to the last result, so we can use getRow on ResultSet
            examRs.last()

            // This is the row of of the last result, so if this is smaller than 0
            if (examRs.row < 1) throw ExamNotFoundException("OfficialExam with ID $id was not found")

            result = OfficialExam(examId = examRs.getInt("ExamID"),
                    durationInMinutes = ((examRs.getTime("EndTime").time / 60000) - (examRs.getTime("StartTime").time / 60000)).toInt(),
                    startTime = Date(examRs.getTimestamp("StartTime").time),
                    endTime = Date(examRs.getTimestamp("EndTime").time),
                    courseId = examRs.getInt("CourseID"),
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

    /**
     * Inserts an officialExam into the database
     *
     * @param officialExam the [OfficialExam] that should be inserted
     * @return the inserted [OfficialExam]
     */
    fun insertExam(officialExam: OfficialExam): OfficialExam {
        val conn: Connection? = MySQLConnection.getConnection()
        val insertExamQuery = "INSERT INTO EXAM (COURSEID, EXAMTYPEID, EXAMCODE, EXAMNAME, STARTTIME, ENDTIME, INSTRUCTIONS, VERSION, LOCATION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        val preparedStatement: PreparedStatement?
        preparedStatement = conn?.prepareStatement(insertExamQuery)
        preparedStatement?.setInt(1, officialExam.courseId)
        preparedStatement?.setInt(2, officialExam.examType.examId)
        preparedStatement?.setString(3, officialExam.name)
        preparedStatement?.setString(4, officialExam.name)
        preparedStatement?.setDate(5, java.sql.Date(officialExam.startTime.time))
        preparedStatement?.setDate(6, java.sql.Date(officialExam.endTime.time))
        preparedStatement?.setString(7, officialExam.instructions)
        preparedStatement?.setInt(8, officialExam.version)
        preparedStatement?.setString(9, officialExam.location)

        try {
            preparedStatement?.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
            throw DatabaseException("Error while interacting with the database")
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
            MySQLConnection.closeConnection(conn)
        }

        return officialExam
    }

    /**
     * Adds single or multiple questions to an officialExam
     *
     * @param officialExam the [OfficialExam] containing all [Question]s
     * @return the updated [OfficialExam]
     */
    fun addQuestionsToExam(officialExam: OfficialExam): OfficialExam {
        if (officialExam.questions == null || officialExam.questions.size < 1) throw DatabaseException("Please provide questions to add to officialExam")

        var query = "INSERT INTO QUESTION_IN_EXAM (EXAMID, QUESTIONID, SEQUENCENUMBER) VALUES "
        for (question in officialExam.questions) {
            query = query.plus("(?, ?, ?)")
            if (question != officialExam.questions.last()) query = query.plus(", ")
        }

        val conn: Connection? = MySQLConnection.getConnection()
        val preparedStatement: PreparedStatement?
        preparedStatement = conn?.prepareStatement(query)

        var index = 0
        for (question in officialExam.questions) {
            preparedStatement?.setInt(++index, officialExam.examId ?: throw DatabaseException("Please provide examID"))
            preparedStatement?.setInt(++index, question.questionId ?: throw DatabaseException("Can't insert question without ID"))
            preparedStatement?.setInt(++index, question.sequenceNumber ?: throw DatabaseException("Can't insert question without sequence number"))
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

        return officialExam
    }
}