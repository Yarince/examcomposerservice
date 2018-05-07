package nl.han.ica.examplatform.business.question

import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.question.QuestionDAOStub
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class QuestionService {

    @Autowired
    lateinit var questionDAO: QuestionDAOStub

    /**
     * Add a new Question to the database
     *
     * @param question [Question] to be added in the database
     * @return ResponseEntity<[Question]> with new question inserted and an assigned id
     */
    fun addQuestion(question: Question): ResponseEntity<Question> {
        return try {
            val insertedQuestion = questionDAO.insertQuestion(question)
            ResponseEntity(insertedQuestion, HttpStatus.CREATED)
        } catch (exception: Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}