package poc

import poc.models.Question
import java.util.concurrent.ThreadLocalRandom

fun main(args: Array<String>) {
    simulateResults(5, 123)
}

private fun simulateResults(amountOfResults: Int, studentNr: Int, iterator: Int = 0, questionsNotAnswered: ArrayList<Question> = ArrayList(), questionsAnswered: ArrayList<Question> = ArrayList()) {
    if (iterator == amountOfResults) return

    // questionsNotAnswered should be all the questions in the course, if there is no exam generated yet
    val exam = generateExam(1, 123, questionsNotAnswered, questionsAnswered)
    // Simulate results
    val results = simulateCorrectAndFalseAnswers(exam, studentNr, iterator)

    // Add questions to answered list
    questionsAnswered.plus(results)

    // Remove just answered questions from list
    questionsNotAnswered.removeIf { questionsAnswered.contains(it) }

    simulateResults(amountOfResults, studentNr, iterator + 1, questionsNotAnswered, questionsAnswered)
}

private fun simulateCorrectAndFalseAnswers(questions: ArrayList<Question>, studentNr: Int, examId: Int): Results {
    val questionResults = questions.map { QuestionResult(questionId = it.questionId, categories = it.categories, resultWasGood = ThreadLocalRandom.current().nextBoolean()) }.toTypedArray()
    return Results(examId, studentNr, questionResults)
}

internal fun generateExam(courseId: Int, studentNr: Int, questionsNotAnswered: ArrayList<Question>, questionsAnswered: ArrayList<Question>): ArrayList<Question> {
    val questionsNotAnsweredUpdated: Array<Question> = if (questionsAnswered.isEmpty()) loadQuestions(courseId, studentNr, "questionBankNotAnswered") else questionsNotAnswered.toTypedArray()

    val ratedCategories = categoriesWithRelevancePercentages(studentNr).toList()
    ratedCategories.forEach { println(it) }
    val exam = addQuestionToExam(studentNr, questionsNotAnsweredUpdated.toCollection(arrayListOf()), questionsAnswered.toCollection(arrayListOf()), ratedCategories, ratedCategories.last())
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