package nl.han.ica.examplatform.models.answermodel

import nl.han.ica.examplatform.models.answermodel.answer.Answer
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.question.Question

/**
 * Represents the answer model that contains the answers for the [Question]s in a [Exam].
 *
 * @param examId [Int] The id of the [Exam] this answer model represents
 * @param answers [ArrayList]<[Answer]> List of correct answers
 */
data class AnswerModel(
        val examId: Int,
        val answers: ArrayList<Answer>? = null
)