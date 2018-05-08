package nl.han.ica.examplatform.persistence.answer

import nl.han.ica.examplatform.models.answer.Answer

interface AnswerDAO {
    fun addAnswerToQuestion(answer: Answer)
}