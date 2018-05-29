package nl.han.ica.examplatform.models.answerModel.answer

/**
 * Represents a word that has to be in the given answer.
 * Each keyword is worth a amount of points.
 *
 * @param keyword [String] The keyword
 * @param points [Int] The amount of points this keyword is worth in a [Answer] of a [Question]
 */
data class Keyword(
    val keyword: String,
    val points: Float
)