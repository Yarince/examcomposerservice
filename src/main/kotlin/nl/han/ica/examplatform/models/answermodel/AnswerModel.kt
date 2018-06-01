package nl.han.ica.examplatform.models.answermodel

import nl.han.ica.examplatform.models.answermodel.answer.Answer
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.models.exam.Exam

/**
 * Represents the answer model that contains the answers for the [Question]s in a [Exam].
 *
 * @param answerModelId [Int] The id of the answer model
 * @param examId [Int] The id of the [Exam] this answer model represents
 * @param answers [ArrayList]<[Answer]> List of correct answers
 */
data class AnswerModel(
        val answerModelId: Int? = null,
        val examId: Int,
        val answers: ArrayList<Answer>? = null
)