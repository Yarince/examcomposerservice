package nl.han.ica.examplatform.models.answermodel

import nl.han.ica.examplatform.models.answermodel.answer.Answer
import java.util.Arrays

/**
 * Represents the answer model that contains the answers for the [Question]s in a [Exam].
 *
 * @param answerModelId [Int] The id of the answer model
 * @param examId [Int] The id of the [Exam] this answer model represents
 * @param answers [Array]<[Answer]> List of correct answers
 */
data class AnswerModel(
    val answerModelId: Int? = null,
    val examId: Int,
    val answers: Array<Answer>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AnswerModel

        if (!Arrays.equals(answers, other.answers)) return false

        return true
    }

    override fun hashCode(): Int {
        return answers?.let { Arrays.hashCode(it) } ?: 0
    }
}