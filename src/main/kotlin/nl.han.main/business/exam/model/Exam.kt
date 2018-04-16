package nl.han.main.business.exam.model

import nl.han.main.business.question.model.Question
import java.util.*

data class Exam(
        val examId: Int,
        val name: String? = null,
        val durationInMinutes: Int? = null,
        val startTime: Date? = null,
        val endTime: Date? = null,
        val course: String? = null,
        val examType: ExamType? = null,
        val instructions: String? = null,
        val questions: Array<Question>? = null
        ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Exam

        if (!Arrays.equals(questions, other.questions)) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}