package poc

import poc.models.Question

/**
 * Here the results of other students on this subject should be analysed.
 * After this the worst performed question should be returned.
 *
 * @param category the category of which the question should be about
 * @return [Question] the first made question of the student
 */
internal fun getMostRelevantNotAssessedQuestionOfCategory(category: String, questions: ArrayList<Question>): Question? {
    val assessedQuestionsOfOthers = loadResultsOfOthers(category)

    // Filter questions to only contain current category
    val questionIdsOfCategory = questions.filter { it.categories.contains(category) }.map { it.questionId }
    val filteredQuestions = assessedQuestionsOfOthers.filter { questionIdsOfCategory.contains(it.questionId) }

    var mostRelevantQuestion: Pair<Int, Double>? = null

    filteredQuestions.forEach {
        val rating = it.nGood.toDouble() / it.nResults

        if (mostRelevantQuestion == null) {
            mostRelevantQuestion = Pair(it.questionId, rating)
        } else {
            if (rating < mostRelevantQuestion!!.second) {
                mostRelevantQuestion = Pair(it.questionId, rating)
            }
        }
    }

    return questions.find { it.questionId == mostRelevantQuestion?.first }
}

/**
 * Here a request should be made to get the question of the category that is the furthest into the past.
 * Probably best to let the db do this
 *
 * @param questionsInCategory the questions in the category
 * @return [Question] the first made question of the student
 */
internal fun getFirstAskedQuestion(questionsInCategory: List<Question>, studentNr: Int): Question {
    return questionsInCategory.sortedBy { it.answeredOn }.first()
}