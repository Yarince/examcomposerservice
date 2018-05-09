package nl.han.ica.examplatform.models.exam

import nl.han.ica.examplatform.models.question.Question
import java.util.*

interface Exam {

    val examId: Int?
    val name: String
    val durationInMinutes: Int
    val startTime: Date
    val endTime: Date
    val courseId: Int
    val version: Int
    val examType: ExamType
    val instructions: String?
    val location: String?
    val questions: ArrayList<Question>?
}