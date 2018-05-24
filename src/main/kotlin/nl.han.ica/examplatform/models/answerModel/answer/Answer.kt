package nl.han.ica.examplatform.models.answerModel.answer

import java.util.Arrays

/**
 * Interface for all classes that represent a correct correctAnswer given by a teacher.
 * This is not the correctAnswer that is given by a student.
 */
data class Answer(
    val questionId: Int,
    val answerKeywords: Array<Keyword>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Answer

        if (!Arrays.equals(answerKeywords, other.answerKeywords)) return false

        return true
    }

    override fun hashCode(): Int {
        return answerKeywords?.let { Arrays.hashCode(it) } ?: 0
    }
}