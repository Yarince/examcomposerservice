package nl.han.ica.examplatform.persistence.question

import nl.han.ica.examplatform.models.question.Question
import org.springframework.stereotype.Repository

@Repository
class QuestionDAO {
    fun insertQuestion(question: Question): Question {
        print(question)
        // Here the database connection should be called on, creating a prepared statement to insert a question
        // This should also be able to throw an exception if it fails
        return question
    }
}