package poc

import poc.models.Question
import java.util.concurrent.ThreadLocalRandom

fun main(args: Array<String>) {
    simulateResults(5, 123, loadQuestions(1, 123, "questionBankNotAnswered").toCollection(arrayListOf()))
}

private fun simulateResults(amountOfResults: Int, studentNr: Int, questionsNotAnswered: ArrayList<Question>, iterator: Int = 0, questionsAnswered: ArrayList<Question> = ArrayList(), previousResults: ArrayList<Results> = ArrayList()) {
    if (iterator == amountOfResults) return

    // questionsNotAnswered should be all the questions in the course, if there is no exam generated yet
    val exam = generateExam(previousResults,1, 123, questionsNotAnswered, questionsAnswered)
    // Simulate results
    val results = simulateCorrectAndFalseAnswers(exam, studentNr, iterator)
    previousResults.add(results)

    // Add questions to answered list
    val newQuestionsAnswered = questionsAnswered.plus(results.questions)

    // Remove just answered questions from list
    // Solution for remove????
    questionsNotAnswered.removeIf { r -> newQuestionsAnswered.any { it.questionId == r.questionId } }

    simulateResults(amountOfResults, studentNr, questionsNotAnswered, iterator + 1, newQuestionsAnswered.toCollection(arrayListOf()), previousResults)
}

private fun simulateCorrectAndFalseAnswers(questions: ArrayList<Question>, studentNr: Int, examId: Int): Results {
    val questionResults = questions.map { Question(questionId = it.questionId, categories = it.categories, wasCorrect = ThreadLocalRandom.current().nextBoolean(), questionText = it.questionText, type = it.type) }.toTypedArray()
    return Results(examId, studentNr, questionResults)
}

internal fun generateExam(previousResults: ArrayList<Results>, courseId: Int, studentNr: Int, questionsNotAnswered: ArrayList<Question>, questionsAnswered: ArrayList<Question>): ArrayList<Question> {

    val ratedCategories = categoriesWithRelevancePercentages(studentNr, previousResults).toList()
    ratedCategories.forEach { println(it) }
    val exam = addQuestionToExam(studentNr, questionsNotAnswered, questionsAnswered.toCollection(arrayListOf()), ratedCategories, ratedCategories.last())
    exam.forEach { println(it) }
    return exam
}

private fun addQuestionToExam(studentNr: Int, notYetAskedQuestions: ArrayList<Question>, alreadyAskedQuestions: ArrayList<Question>, ratedCategories: List<Pair<String, Double>>, currentCategory: Pair<String, Double>, questionsInExam: ArrayList<Question> = ArrayList()): ArrayList<Question> {
    // Return if the category is not in the list with categories
    if (!ratedCategories.contains(currentCategory)) return questionsInExam
    // Return if the prerequisites are met
    if (checkIfExamCompliesToPrerequisites(questionsInExam, notYetAskedQuestions)) return questionsInExam

    // If there are no questions available, it should be returned
    if (ratedCategories.isEmpty()) return questionsInExam

    val ratedCategoriesWithoutEmptyQuestions = ratedCategories.toMutableList()
    if (questionOfCategoryWillBeAdded(currentCategory.second)) {

        var questionToAdd = getMostRelevantNotAssessedQuestionOfCategory(currentCategory.first, notYetAskedQuestions)
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
        notYetAskedQuestions.remove(questionToAdd)
    }

    val indexOfCurrentCategory = ratedCategories.indexOf(currentCategory)
    val nextCategory = if (indexOfCurrentCategory == 0)
    // go back to most relevant category
        ratedCategories.last()
    else
        ratedCategories[indexOfCurrentCategory - 1]


    // Recursively add more questions
    return addQuestionToExam(studentNr, notYetAskedQuestions, alreadyAskedQuestions, ratedCategoriesWithoutEmptyQuestions, nextCategory, questionsInExam)
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

    return exam.size >= maxAmountOfQuestionsInExam
}