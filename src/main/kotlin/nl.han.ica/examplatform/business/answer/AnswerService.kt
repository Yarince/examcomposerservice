package nl.han.ica.examplatform.business.answer

import nl.han.ica.examplatform.controllers.responseexceptions.CouldNotAddAnswerToQuestionException
import nl.han.ica.examplatform.models.answer.Answer
import nl.han.ica.examplatform.persistence.answer.AnswerDAO
import org.springframework.stereotype.Service

@Service
class AnswerService(private val answerDao: AnswerDAO) {

    fun addAnswerToQuestion(answer: Answer) {
        try {
            answerDao.addOpenAnswerToQuestion(answer)
        } catch (exception: Exception) {
            throw CouldNotAddAnswerToQuestionException("Answer could not be added to Question", exception, true, true)
        }
    }
}