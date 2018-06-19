package nl.han.ica.examplatform.models.question

/**
 * Statistics about a question.
 *
 * @param questionId [Int] the ID of the question
 * @param nResults [Int] the amount of times the question has been answered
 * @param nCorrect [Int] the amount of times the question was answered correctly
 * @param nWrong [Int] the amount of times the question was answered wrongly
 */
data class QuestionResultStats(
        val questionId: Int,
        val nResults: Int,
        val nCorrect: Int,
        val nWrong: Int
)
