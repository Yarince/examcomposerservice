package nl.han.ica.examplatform.models.answer

/**
 * Interface for all classes that represent a correct correctQuestionAnswer given by a teacher.
 * This is not the correctQuestionAnswer that is given by a student.
 */
interface Answer {

    val questionId: Int
    val correctQuestionAnswer: String
    val answerKeywords: Keywords?
}