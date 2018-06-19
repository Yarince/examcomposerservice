package nl.han.ica.examplatform.models.question

import java.util.*

/**
 * This represents an assessed question of a practice exam.
 *
 * @param questionId [Int] the question ID
 * @param questionText [String] the question text
 * @param categories [ArrayList]<[String]> the categories in the question
 * @param type [String] the type of the question
 * @param submittedExamId [Int] the ID of the exam
 * @param wasCorrect [Boolean] indicates if the answer was correct
 */
data class QuestionResult(
        val questionId: Int,
        val questionText: String,
        val categories: ArrayList<String>,
        val type: String,
        val submittedExamId: Int,
        val wasCorrect: Boolean
)
