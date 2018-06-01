package nl.han.ica.examplatform.models.exam

import nl.han.ica.examplatform.models.course.Course

// This DTO is needed for returning a list of all exams without details.
/**
 * Represents a simple form of a [Exam].
 * Only contains the basic information of a Exam
 *
 * @param examId [Int] The id of the [Exam]
 * @param name [String] The name of the [Exam]
 * @param courseId [Int] The is of the [Course] the [Exam] is in.
 */
data class SimpleExam(
    val examId: Int,
    val name: String,
    val courseId: Int
)
