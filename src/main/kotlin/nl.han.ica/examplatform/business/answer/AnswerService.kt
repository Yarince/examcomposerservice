package nl.han.ica.examplatform.business.answer

import nl.han.ica.examplatform.controllers.responseexceptions.CouldNotAddAnswerToQuestionException
import nl.han.ica.examplatform.models.answerModel.answer.Answer
import nl.han.ica.examplatform.persistence.answer.AnswerDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Answer service for handling requests related to the [Answer] model.
 */
@Service
class AnswerService {

    @Autowired
    private lateinit var answerDao: AnswerDAO

    /**
     * Add an Answer to a Question.
     *
     * @param answer The [Answer] you want to add
     */
    fun addAnswerToQuestion(answer: Answer) {
        try {
            answerDao.addAnswerToQuestion(answer)
        } catch (exception: RuntimeException) {
            throw CouldNotAddAnswerToQuestionException(exception.message, exception, true, true)
        }
    }
}
