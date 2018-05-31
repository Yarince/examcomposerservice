package nl.han.ica.examplatform.business.answer

import nl.han.ica.examplatform.models.answermodel.answer.Answer
import nl.han.ica.examplatform.persistence.answer.AnswerDAO
import nl.han.ica.examplatform.persistence.answer.IAnswerDAO
import org.springframework.stereotype.Service

/**
 * Answer service for handling requests related to the [Answer] model.
 *
 * @param answerDAO [AnswerDAO] The AnswerDAO
 */
@Service
class AnswerService(private val answerDAO: IAnswerDAO) {

    /**
     * Add an Answer to a Question.
     *
     * @param answer The [Answer] you want to add
     */
    fun addOrUpdateAnswerInQuestion(answer: Answer) =
            answerDAO.addOrUpdateAnswerInQuestion(answer)

    fun addOrUpdateAnswerInQuestionInExam(answer: Answer, examId: Int) =
            answerDAO.addOrUpdateAnswerInQuestionInExam(answer, examId)
}
