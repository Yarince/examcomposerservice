package poc

/**
 * Here the results of other students on this subject should be analysed.
 * After this the worst performed question should be returned.
 *
 * @param category the category of which the question should be about
 * @return [Question] the first made question of the student
 */
internal fun getMostRelevantNotAssessedQuestionOfCategory(category: String): Question? {
    return null
}

/**
 * Here a request should be made to get the question of the category that is the furthest into the past.
 * Probably best to let the db do this
 *
 * @param category the category of which the question should be about
 * @return [Question] the first made question of the student
 */
internal fun getFirstAskedQuestion(category: String, studentNr: Int): Question {
    val questions = loadQuestions(1, studentNr, true)

    val questionsInCategory = ArrayList<Question>()
    for (question in questions) {
        if (question.categories.contains(category)) {
            questionsInCategory.add(question)
        }
    }

    questionsInCategory.sortBy { it.answeredOn }
    return questionsInCategory.first()
}