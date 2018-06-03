package poc

import java.util.concurrent.ThreadLocalRandom

fun main(args: Array<String>) {
    generateExam(1, 1)
}

fun generateExam(courseId: Int, studentNr: Int) {
    val questions = loadQuestions(courseId, studentNr)
    val ratedCategories = questionsToCategoryRating(questions)

    ratedCategories.forEach { println(it) }
}

private fun addQuestionToExam(studentNr: Int, allQuestions: Array<Question>, ratedCategories: HashMap<String, Double>, currentCategory: String) {
    if (!ratedCategories.containsKey(currentCategory)) return

    if (questionOfCategoryWillBeAdded(ratedCategories[currentCategory]!!)) {

        val questionToAdd = getMostRelevantNotAssessedQuestionOfCategory(currentCategory)
                ?: getFirstAskedQuestion(currentCategory, studentNr)

        //todo: add question to exam
    } else {
        val nextCategory = "Todo" // todo: determine next category
        addQuestionToExam(studentNr, allQuestions, ratedCategories, nextCategory)
    }
}

/**
 * Determines if a question of said category will be added based on the chance it has and a random number.
 */
private fun questionOfCategoryWillBeAdded(chanceToGetAdded: Double): Boolean {
    val randomNumber = ThreadLocalRandom.current().nextDouble(0.0, 99.99)
    return randomNumber < chanceToGetAdded
}