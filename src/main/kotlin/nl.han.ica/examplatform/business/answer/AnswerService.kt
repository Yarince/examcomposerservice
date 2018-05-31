package nl.han.ica.examplatform.business.answer

import nl.han.ica.examplatform.models.answermodel.answer.Answer
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.answer.AnswerDAO
import nl.han.ica.examplatform.persistence.answer.IAnswerDAO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    /**
     * Add an Answer to a Question in an [Exam].
     *
     * @param answer The [Answer] you want to add
     */
    fun addOrUpdateAnswerInQuestionInExam(answer: Answer, examId: Int) =
            answerDAO.addOrUpdateAnswerInQuestionInExam(answer, examId)

    /**
     * Get an Answer by question from database
     *
     * @param questionId [Int] The ID of the [Question]
     */
    fun getAnswerForQuestion(questionId: Int) =
            ResponseEntity(answerDAO.getAnswerForQuestion(questionId), HttpStatus.OK)

    /**
     * Get an AnswerModel for an Exam from database
     *
     * @param examId [Int] The ID of the [Exam]
     */
    fun getAnswersForExam(examId: Int) =
            ResponseEntity(answerDAO.getAnswersForExam(examId), HttpStatus.OK)
}
