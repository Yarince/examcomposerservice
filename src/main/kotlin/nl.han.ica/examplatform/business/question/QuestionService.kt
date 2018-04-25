package nl.han.ica.examplatform.business.question

import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.question.QuestionDAOStub
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import javax.xml.ws.Response

@Service
class QuestionService {

    @Autowired
    lateinit var questionDAO: QuestionDAOStub

    fun addQuestion(question: Question): ResponseEntity<Question> {
        return try {
            val insertedQuestion = questionDAO.insertQuestion(question)
            print("Inserted question")
            print(insertedQuestion)
            ResponseEntity(insertedQuestion, HttpStatus.CREATED)
        } catch (exception: Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    fun checkIfQuestionIsAvailableInDatabase(question: Question) : ResponseEntity<Boolean> {
        return try {
            val check = questionDAO.exists(question)
            print("Question in database: ")
            print(check)
            ResponseEntity(check, HttpStatus.CREATED)
        } catch (exception: Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }


    }
}