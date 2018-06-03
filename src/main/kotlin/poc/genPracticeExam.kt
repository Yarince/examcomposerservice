package poc

import java.util.concurrent.ThreadLocalRandom

fun main(args: Array<String>) {
    generateExam(1, 1)
}

fun generateExam(courseId: Int, studentNr: Int) {
    val questions = loadQuestions(courseId, studentNr)
    val ratedCategories = questionsToSortedCategoryRating(questions)

    ratedCategories.forEach { println(it) }
}

private fun addQuestionToExam(studentNr: Int, allQuestions: ArrayList<Question>, ratedCategories: List<Pair<String, Double>>, currentCategory: Pair<String, Double>, questionsInExam: ArrayList<Question> = ArrayList()) {
    // Return if the category is not in the list with categories
    if (!ratedCategories.contains(currentCategory)) return

    if (questionOfCategoryWillBeAdded(currentCategory.second)) {

        val questionToAdd = getMostRelevantNotAssessedQuestionOfCategory(currentCategory.first)
                ?: getFirstAskedQuestion(currentCategory.first, studentNr)

        questionsInExam.add(questionToAdd)
        allQuestions.remove(questionToAdd)
        //todo: add question to exam
    } else {
        val nextCategory = Pair("Todo", 0.0) // todo: determine next category
        addQuestionToExam(studentNr, allQuestions, ratedCategories, nextCategory, questionsInExam)
    }

    // Determine next category
}

/**
 * Determines if a question of said category will be added based on the chance it has and a random number.
 */
private fun questionOfCategoryWillBeAdded(chanceToGetAdded: Double): Boolean {
    val randomNumber = ThreadLocalRandom.current().nextDouble(0.0, 99.99)
    return randomNumber < chanceToGetAdded
}