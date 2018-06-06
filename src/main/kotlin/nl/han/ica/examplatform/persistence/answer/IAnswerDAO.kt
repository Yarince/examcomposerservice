package nl.han.ica.examplatform.persistence.answer

import nl.han.ica.examplatform.models.answermodel.AnswerModel
import nl.han.ica.examplatform.models.answermodel.answer.Answer
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.question.Question

/**
 * Interface that has to be implemented by all Answers.
 */
interface IAnswerDAO {

    /**
     * Add or update an Answer for a [Question] in the database.
     *
     * @param answer [Answer] The Answer you want to add to a Question
     */
    fun addOrUpdateAnswerInQuestion(answer: Answer)

    /**
     * Add or Update an Answer for a [Question] in an [Exam] in the database
     *
     * @param answer [Answer] The Answer you want to add to a Question in an Exam
     * @param examId [Int] The ID of the exam you want to add the Answer to
     */
    fun addOrUpdateAnswerInQuestionInExam(answer: Answer, examId: Int)

    /**
     * Retrieve an Answer for a question from the database
     *
     * @param questionId [Int] The id of the question
     */
    fun getAnswerForQuestion(questionId: Int): Answer

    /**
     * Get answerModel from database by [Exam]
     *
     * @param examId [Int] The ID of the exam
     */
    fun getAnswersForExam(examId: Int): AnswerModel
}
