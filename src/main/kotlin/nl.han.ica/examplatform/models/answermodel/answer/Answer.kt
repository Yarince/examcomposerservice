package nl.han.ica.examplatform.models.answermodel.answer

import nl.han.ica.examplatform.models.question.Question
import java.util.*

/**
 * Represents a correct answer given by a teacher.
 * This is <b>not</b> a answer that is given by a student.
 *
 * @param questionId [Int] The id of the [Question] this answer is for.
 * @param answerKeywords [Array]<[Keyword]> List of keywords that need to be in the answer.
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

    override fun hashCode(): Int = answerKeywords?.let { Arrays.hashCode(it) } ?: 0
}
