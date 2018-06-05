package poc

import java.util.concurrent.ThreadLocalRandom

fun main(args: Array<String>) {
//    generateExam(1, 1)
    genTestData()
}

fun generateExam(courseId: Int, studentNr: Int) {
    val questions = loadQuestions(courseId, studentNr)
    val alreadyAskedQuestions = loadQuestions(1, studentNr, true)
    val ratedCategories = questionsToSortedCategoryRating(questions)

    ratedCategories.forEach { println(it) }

    val exam = addQuestionToExam(studentNr, questions.toCollection(arrayListOf()), alreadyAskedQuestions.toCollection(arrayListOf()), ratedCategories, ratedCategories.last())
    exam.forEach { println(it) }
}

private fun addQuestionToExam(studentNr: Int, allQuestions: ArrayList<Question>, alreadyAskedQuestions: ArrayList<Question>, ratedCategories: List<Pair<String, Double>>, currentCategory: Pair<String, Double>, questionsInExam: ArrayList<Question> = ArrayList()): ArrayList<Question> {
    // Return if the category is not in the list with categories
    if (!ratedCategories.contains(currentCategory)) return questionsInExam
    // Return if the prerequisites are met
    if (checkIfExamCompliesToPrerequisites(questionsInExam, allQuestions)) return questionsInExam

    // If there are no questions available, it should be returned
    if (allQuestions.isEmpty()) return questionsInExam
    if (ratedCategories.isEmpty()) return questionsInExam

    val ratedCategoriesWithoutEmptyQuestions = ratedCategories.toMutableList()
    if (questionOfCategoryWillBeAdded(currentCategory.second)) {

        var questionToAdd = getMostRelevantNotAssessedQuestionOfCategory(currentCategory.first)
        if (questionToAdd == null) {
            val alreadyAskedQuestionsInCurrentCategory = alreadyAskedQuestions.filter { it.categories.contains(currentCategory.first) }
            if (alreadyAskedQuestionsInCurrentCategory.isNotEmpty())
                questionToAdd = getFirstAskedQuestion(alreadyAskedQuestionsInCurrentCategory, studentNr)
            else
                // No assessed questions are available, and no already asked questions are available
                // Thus, the category should be removed
                ratedCategoriesWithoutEmptyQuestions.remove(currentCategory)
        }

        questionToAdd?.let { questionsInExam.add(it) }
        alreadyAskedQuestions.remove(questionToAdd)
        allQuestions.remove(questionToAdd)
    }

    val indexOfCurrentCategory = ratedCategories.indexOf(currentCategory)
    val nextCategory = if (indexOfCurrentCategory == 0)
        // go back to most relevant category
        ratedCategories.last()
    else
        ratedCategories[indexOfCurrentCategory - 1]


    // Recursively add more questions
    return addQuestionToExam(studentNr, allQuestions, alreadyAskedQuestions, ratedCategoriesWithoutEmptyQuestions, nextCategory, questionsInExam)
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


private fun genTestData() {
    val nRes = arrayOf(4, 2, 10, 20, 16)
    val nGood = arrayOf(4, 1, 2, 4, 14)
    val nWrong = arrayOf(0, 1, 8, 16, 2)
    val answers = arrayOf(true, false, true, true, false, false, false, true)

    for (i in 1..5) {
        println("{\"examId\": $i,")
        println("\"questions\": [")
        for (x in i..i+10) {
            println("{\"questionId\": $x,")
            println("\"resultWasGood\": ${answers[x%8]}},")
        }
        println("],")
    }
}