package nl.han.ica.examplatform.models.answerModel

import nl.han.ica.examplatform.models.answerModel.answer.Answer
import java.util.Arrays

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