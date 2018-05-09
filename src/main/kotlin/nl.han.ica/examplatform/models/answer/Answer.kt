package nl.han.ica.examplatform.models.answer

/**
 * Interface for all classes that represent a correct correctAnswer given by a teacher.
 * This is not the correctAnswer that is given by a student.
 */
interface Answer {

    val questionId: Int
    val correctAnswer: String
    val answerKeywords: Keywords?
}