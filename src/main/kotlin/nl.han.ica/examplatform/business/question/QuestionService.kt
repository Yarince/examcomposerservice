package nl.han.ica.examplatform.business.question

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class QuestionService {
    private val logger = loggerFor(javaClass)

    @Autowired
    private lateinit var questionDAO: QuestionDAO

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
            logger.error("Couldn't insert question: $question")
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}