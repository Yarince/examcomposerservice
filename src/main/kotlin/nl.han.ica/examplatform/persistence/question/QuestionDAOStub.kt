package nl.han.ica.examplatform.persistence.question

import nl.han.ica.examplatform.models.question.Question
import org.springframework.stereotype.Repository

@Repository
class QuestionDAOStub {
    fun insertQuestion(question: Question): Question {
        print(question)
        // Here the database connection should be called on, creating a prepared statement to insert a question
        // This should also be able to throw an exception if it fails
        return question
    }

    fun exists(question: Question): Boolean {
        val questionDB = Array(1, { question })

        // check if a question exists in the database
        return question in questionDB
    }
}