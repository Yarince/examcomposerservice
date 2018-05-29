package nl.han.ica.examplatform.models.exam

import java.util.*

data class PracticeExamRequestBody(
        val courseId: Int,
        val categories: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PracticeExamRequestBody

        if (!Arrays.equals(categories, other.categories)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(categories)
    }
}