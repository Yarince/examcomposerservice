package nl.han.main.persistence.question

import nl.han.main.model.question.Question
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