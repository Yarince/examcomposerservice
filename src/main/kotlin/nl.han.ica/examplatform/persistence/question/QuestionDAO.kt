package nl.han.ica.examplatform.persistence.question

import nl.han.ica.examplatform.controllers.responseexceptions.DatabaseException
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Database access object that handles all database queries regarding [Question]
 */
@Repository
class QuestionDAO {

    /**
     * Adds a question to the database
     */
    fun insertQuestion(question: Question): Question {
        var questionToReturn = question
        var dbConnection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        // Todo: change insert string. To match with questionModel and Database [BTGGOM-460]
        val sqlQueryStringInsertQuestionString = "INSERT INTO QUESTION (QUESTIONID, QUESTIONTEXT, QUESTIONTYPE, SEQUENCENUMBER) VALUES (?, ?, ?, ?)"
        try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(sqlQueryStringInsertQuestionString)
            preparedStatement?.setInt(1, question.questionId ?: 0)
            preparedStatement?.setString(2, question.questionText)
            preparedStatement?.setString(3, question.questionType)
            if (question.questionOrderInExam == null) preparedStatement?.setNull(4, java.sql.Types.INTEGER) else preparedStatement?.setInt(4, question.questionOrderInExam)

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
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            MySQLConnection.closeStatement(preparedStatement)
        }
        return questionToReturn
    }

    /**
     * Checks if a question already exists in the database
     */
    fun exists(question: Question?): Boolean {
        var dbConnection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        val sqlQueryStringSelectIfQuestionExistsString = "SELECT QUESTIONTEXT FROM QUESTION WHERE QUESTIONID = ?"
        try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(sqlQueryStringSelectIfQuestionExistsString)
            preparedStatement?.setInt(1, question?.questionId ?: 0)
            val rs = preparedStatement?.executeQuery()
            if (rs?.next() == true) {
                return true
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            print(e)
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            preparedStatement?.close()
        }
        return false
    }

    /**
     * Gets all questions of a course
     *
     * @param courseId [Int] The ID of course of which the questions should be retrieved
     * @return [Array]<[Question]> An array of all questions corresponding to the course
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
     * Get a question from the database by question ID.
     *
     * @param questionId [Int] The ID of Question wich should be retrieved.
     * @return [Array]<[Question]> A question corresponding to the id.
     */
    fun getQuestion(questionId: Int): Question {
        return Question(questionId = 1,
                questionText = "Stub",
                questionType = "Stub")
    }
}
