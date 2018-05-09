package nl.han.ica.examplatform.models.answer

/**
 * Interface for all classes that represent a correct answer given by a teacher.
 * This is not the answer that is given by a student.
 */
interface Answer {

    val questionId: Int
    val answer: String
    val answerKeywords: Keywords?
}