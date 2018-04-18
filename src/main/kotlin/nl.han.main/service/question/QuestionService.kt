package nl.han.main.service.question

import nl.han.main.model.question.Question
import nl.han.main.persistence.question.QuestionDAO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class QuestionService {
    lateinit var questionDAO: QuestionDAO

    fun addQuestion(question: Question): ResponseEntity<Question> {
        return try {
            ResponseEntity(questionDAO.insertQuestion(question), HttpStatus.CREATED)
        } catch (exception: Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}