package nl.han.ica.examplatform.persistence.question

import nl.han.ica.examplatform.models.question.Question
import org.springframework.stereotype.Repository

@Repository
class QuestionDAOStub {

    // var fetchDatabaseConnection : MySQLDatabaseConnection


    fun insertQuestion(question: Question) {
       /*
       var sqlQuery = "INSERT INTO QUESTION (PARENTQUESTIONID, COURSECODE, QUESTIONTEXT, TYPE, SEQUENCENUMBER, ANSWERTEXT, ANSWERKEYWORDS, ASSESSMENTCOMMENTS)
                       VALUES (NULL, 1, NULL, question.questionText, question.questionType, NULL, NULL, NULL, NULL)"
       var databaseConnection : Connection? = fetchDatabaseConnection.getConnection()
       try {
       var ps: PreparedStatement? = databaseConnection.prepareStatement(sqlQuery)


       }
        */
    }
}