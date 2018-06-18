package nl.han.ica.examplatform.business.exam.practice


import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.question.IQuestionDAO
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

internal fun generatePersonalExam(previousResults: ArrayList<Results>, courseId: Int, studentNr: Int, categories: ArrayList<String>, questionDAO: IQuestionDAO): ArrayList<Question> {
    val questionsNotAnswered: ArrayList<Question> = questionDAO.getQuestionsNotAnsweredByStudentInCourse(studentNr, courseId)
    val questionsAnswered: ArrayList<Question> = questionDAO.getQuestionsAnsweredByStudentInCourse(studentNr, courseId)

    val ratedCategories = categoriesWithRelevancePercentages(studentNr, previousResults, categories)
    ratedCategories.forEach { println(it) }
    return addQuestionToExam(studentNr, questionsNotAnswered, questionsAnswered.toCollection(arrayListOf()), ratedCategories, ratedCategories.last())
}

private fun addQuestionToExam(studentNr: Int, notYetAskedQuestions: ArrayList<Question>, alreadyAskedQuestions: ArrayList<Question>, ratedCategories: List<Pair<String, Double>>, currentCategory: Pair<String, Double>, questionsInExam: ArrayList<Question> = ArrayList()): ArrayList<Question> {
    // Return if the category is not in the list with categories
    if (!ratedCategories.contains(currentCategory))
        return questionsInExam
    // Return if the prerequisites are met
    if (checkIfExamCompliesToPrerequisites(questionsInExam, notYetAskedQuestions.size + alreadyAskedQuestions.size))
        return questionsInExam

    // If there are no questions available, it should be returned
    if (ratedCategories.isEmpty())
        return questionsInExam

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

        questionsInExam.add(questionToAdd)
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
private fun checkIfExamCompliesToPrerequisites(exam: ArrayList<Question>, allQuestionsSize: Int): Boolean {
    val thresholdForPercentage = 0
    val percentageOfQuestionsInExam = 0.33
    val maxAmountOfQuestionsInExam = if (allQuestionsSize < thresholdForPercentage) (allQuestionsSize * percentageOfQuestionsInExam).toInt() else 10

    return exam.size >= maxAmountOfQuestionsInExam
}