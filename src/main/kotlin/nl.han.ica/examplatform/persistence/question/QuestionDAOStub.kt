package nl.han.ica.examplatform.persistence.question

import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

@Repository
class QuestionDAOStub {

    var database = MySQLConnection
    var dbConnection : Connection? = null
    var preparedStatement : PreparedStatement? = null


    fun insertQuestion(question: Question) : Question {
        val sqlQueryStringInsertQuestionString = "INSERT INTO QUESTION (QUESTIONID, PARENTQUESTIONID, EXAMTYPEID, COURSEID, QUESTIONTEXT, QUESTIONTYPE, SEQUENCENUMBER, ANSWERTEXT, ANSWERKEYWORDS, ASSESSMENTCOMMENTS) " +
                "VALUES (${question.questionId}, ${question.parentQuestionId},${question.examTypeId.value},${question.courseId.value},\"${question.questionText}\",\"${question.questionType}\",${question.sequenceNumber},\"${question.answerText}\",\"${question.answerKeywords}\",\"${question.assessmentComments}\")"
      try {
          dbConnection = MySQLConnection.getConnection()
          preparedStatement = dbConnection?.prepareStatement(sqlQueryStringInsertQuestionString)
          preparedStatement?.executeUpdate()
      } catch (e : SQLException) {
          e.printStackTrace()
      } finally {
          database.closeConnection(dbConnection)
      }
        return question
    }

    fun exists(question: Question?): Boolean {
        val sqlQueryStringSelectIfQuestionExistsString = "SELECT QUESTIONTEXT FROM QUESTION WHERE QUESTIONID = ${question?.questionId}"
        try {
            dbConnection = MySQLConnection.getConnection()
            preparedStatement = dbConnection?.prepareStatement(sqlQueryStringSelectIfQuestionExistsString)
            var rs : ResultSet? = preparedStatement?.executeQuery()
            if(rs?.next() == true) {
                return true
            }
        } catch (e : SQLException) {
            e.printStackTrace()
        } finally {
            database.closeConnection(dbConnection)
        }
        return false
    }
}