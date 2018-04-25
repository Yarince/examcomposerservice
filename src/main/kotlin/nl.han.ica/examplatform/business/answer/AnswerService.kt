package nl.han.ica.examplatform.business.answer

import nl.han.ica.examplatform.controllers.responseexceptions.CouldNotAddAnswerToQuestionException
import nl.han.ica.examplatform.models.answer.Answer
import nl.han.ica.examplatform.persistence.answer.AnswerDAO
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class AnswerService(private val answerDao: AnswerDAO) {
    companion object {
        val LOG: Logger = Logger.getLogger(this::class.java.name)
    }

    fun addAnswerToQuestion(answer: Answer) {
        try {
            answerDao.addAnswerToQuestion(answer)
        } catch (exception: Exception) {
            LOG.warning(exception.message)
            throw CouldNotAddAnswerToQuestionException("Answer could not be added to Question", exception, true, true)
        }
    }
}