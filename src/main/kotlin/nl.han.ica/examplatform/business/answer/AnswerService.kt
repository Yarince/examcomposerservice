package nl.han.ica.examplatform.business.answer

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.responseexceptions.CouldNotAddAnswerToQuestionException
import nl.han.ica.examplatform.models.answerModel.answer.Answer
import nl.han.ica.examplatform.persistence.answer.AnswerDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AnswerService {
    private val logger = loggerFor(javaClass)

    @Autowired
    private lateinit var answerDao: AnswerDAO

    /**
     * Add an Answer to a Question
     *
     * @param answer The [Answer] you want to add

     */
    fun addAnswerToQuestion(answer: Answer) {
        try {
            answerDao.addAnswerToQuestion(answer)
        } catch (exception: RuntimeException) {
            logger.error("Could not add answer to question")
            throw CouldNotAddAnswerToQuestionException(exception.message, exception, true, true)
        }
    }
}