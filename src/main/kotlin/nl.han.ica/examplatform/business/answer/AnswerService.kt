package nl.han.ica.examplatform.business.answer

import nl.han.ica.examplatform.controllers.responseexceptions.CouldNotAddAnswerToQuestionException
import nl.han.ica.examplatform.models.answermodel.answer.Answer
import nl.han.ica.examplatform.persistence.answer.AnswerDAO
import org.springframework.stereotype.Service

/**
 * Answer service for handling requests related to the [Answer] model.
 *
 * @param answerDAO [AnswerDAO] The AnswerDAO
 */
@Service
class AnswerService(private val answerDAO: AnswerDAO) {

    /**
     * Add an Answer to a Question.
     *
     * @param answer The [Answer] you want to add
     */
    fun addAnswerToQuestion(answer: Answer) =
        try {
            answerDAO.addAnswerToQuestion(answer)
        } catch (exception: RuntimeException) {
            throw CouldNotAddAnswerToQuestionException(exception.message, exception)
        }
}
