package nl.han.main.business.question.model

import java.util.*

data class Question (
        val questionId: Int,
        val questionType: QuestionType? = null,
        val questionText: String? = null,
        val subQuestions: Array<Question>? = null
        ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question

        if (!Arrays.equals(subQuestions, other.subQuestions)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(subQuestions)
    }
}