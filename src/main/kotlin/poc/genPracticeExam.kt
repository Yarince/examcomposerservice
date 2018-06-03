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

private fun addQuestionToExam(studentNr: Int, allQuestions: ArrayList<Question>, ratedCategories: List<Pair<String, Double>>, currentCategory: Pair<String, Double>, questionsInExam: ArrayList<Question> = ArrayList()): ArrayList<Question> {
    // Return if the category is not in the list with categories
    if (!ratedCategories.contains(currentCategory)) return questionsInExam
    // Return if the prerequisites are met
    if (checkIfExamCompliesToPrerequisites(questionsInExam, allQuestions)) return questionsInExam

    if (questionOfCategoryWillBeAdded(currentCategory.second)) {

        val questionToAdd = getMostRelevantNotAssessedQuestionOfCategory(currentCategory.first)
                ?: getFirstAskedQuestion(currentCategory.first, studentNr)

        questionsInExam.add(questionToAdd)
        allQuestions.remove(questionToAdd)
    }

    val indexOfCurrentCategory = ratedCategories.indexOf(currentCategory)
    val nextCategory = if (indexOfCurrentCategory == 0) {
        // go back to most relevant category
        ratedCategories.last()
    } else {
        ratedCategories[indexOfCurrentCategory - 1]
    }

    // Recursively add more questions
    return addQuestionToExam(studentNr, allQuestions, ratedCategories, nextCategory, questionsInExam)
}

/**
 * Determines if a question of said category will be added based on the chance it has and a random number.
 */
private fun questionOfCategoryWillBeAdded(chanceToGetAdded: Double): Boolean {
    val randomNumber = ThreadLocalRandom.current().nextDouble(0.0, 99.99)
    return randomNumber < chanceToGetAdded
}

/**
 * Checks if the exam has enough questions or meets other demands
 */
private fun checkIfExamCompliesToPrerequisites(exam: ArrayList<Question>, allQuestions: ArrayList<Question>): Boolean {
    val thresholdForPercentage = 30
    val percentageOfQuestionsInExam = 0.33
    val maxAmountOfQuestionsInExam = if (allQuestions.size < thresholdForPercentage) (allQuestions.size * percentageOfQuestionsInExam).toInt() else 10

    return exam.size > maxAmountOfQuestionsInExam
}