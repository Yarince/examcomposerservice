package nl.han.ica.examplatform.persistence.answer

import nl.han.ica.examplatform.models.answerModel.answer.Answer

interface IAnswerDAO {

    fun addAnswerToQuestion(answer: Answer)
}