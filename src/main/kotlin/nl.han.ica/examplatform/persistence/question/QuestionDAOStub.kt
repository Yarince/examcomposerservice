package nl.han.ica.examplatform.persistence.question

import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

@Repository
class QuestionDAOStub {

    var dbConnection : Connection? = null
    var preparedStatement : PreparedStatement? = null


    fun insertQuestion(question: Question) : Question {
        val sqlQueryStringInsertQuestionString = "INSERT INTO QUESTION (QUESTIONID, PARENTQUESTIONID, EXAMTYPEID, COURSEID, QUESTIONTEXT, QUESTIONTYPE, SEQUENCENUMBER, ANSWERTEXT, ANSWERKEYWORDS, ASSESSMENTCOMMENTS) " +
          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
          try {
              dbConnection = MySQLConnection.getConnection()
              preparedStatement = dbConnection?.prepareStatement(sqlQueryStringInsertQuestionString)
              preparedStatement?.setInt(1, question.questionId?:0)
          preparedStatement?.setInt(2, question.parentQuestionId?:0)
          preparedStatement?.setInt(3, question.examTypeId.value)
          preparedStatement?.setInt(4, question.courseId.value)
          preparedStatement?.setString(5, question.questionText)
          preparedStatement?.setString(6, question.questionType.toString())
          preparedStatement?.setInt(7, question.sequenceNumber?:0)
          preparedStatement?.setString(8, question.answerText)
          preparedStatement?.setString(9, question.answerKeywords)
          preparedStatement?.setString(10, question.assessmentComments)
          preparedStatement?.executeUpdate()
      } catch (e : SQLException) {
          e.printStackTrace()
      } finally {
          MySQLConnection.closeConnection(dbConnection)
          preparedStatement?.close()
      }
        return question
    }

    fun exists(question: Question?): Boolean {
        val sqlQueryStringSelectIfQuestionExistsString = "SELECT QUESTIONTEXT FROM QUESTION WHERE QUESTIONID = ?"
        try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(sqlQueryStringSelectIfQuestionExistsString)
            preparedStatement?.setInt(1, question?.questionId?:0)
            var rs = preparedStatement?.executeQuery()
            if(rs?.next() == true) {
                return true
            }
        } catch (e : SQLException) {
            e.printStackTrace()
        } finally {
            MySQLConnection.closeConnection(dbConnection)
            preparedStatement?.close()
        }
        return false
    }
}