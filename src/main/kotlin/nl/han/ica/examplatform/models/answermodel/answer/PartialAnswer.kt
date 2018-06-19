package nl.han.ica.examplatform.models.answermodel.answer

import nl.han.ica.examplatform.models.question.Question

/**
 * Represents a word that has to be in the given answer.
 * Each partialAnswers is worth a amount of points.
 *
 * @param id [Int] The ID of the partial answer
 * @param text [String] The text of the partial answer
 * @param points [Int] The amount of points this partialAnswers is worth in a [Answer] of a [Question]
 */
data class PartialAnswer(
        var id: Int? = null,
        val text: String,
        val points: Int? = null
)
