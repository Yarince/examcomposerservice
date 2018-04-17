package nl.han.ica.examplatform.models.exam

import nl.han.ica.examplatform.models.question.Question
import java.util.Date

data class Exam(
        val examId: Int? = null,
        val name: String? = null,
        val durationInMinutes: Int? = null,
        val startTime: Date? = null,
        val endTime: Date? = null,
        val course: String? = null,
        val examType: ExamType? = null,
        val instructions: String? = null,
        val location: String? = null,
        val questions: Array<Question>? = null
        )