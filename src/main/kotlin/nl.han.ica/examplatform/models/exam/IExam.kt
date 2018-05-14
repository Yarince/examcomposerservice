package nl.han.ica.examplatform.models.exam

import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.question.Question

interface IExam {
    val name: String
    val courseId: Int
    val version: Int?
    val examType: ExamType
    val questions: ArrayList<Question>?
}