package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.models.question.AnswerdQuestion
import nl.han.ica.examplatform.models.question.Question
import kotlin.math.log10
import kotlin.math.sqrt

fun contentBasedFiltering(
        allQuestions: Array<Question>,
        answeredQuestions: Array<AnswerdQuestion>,
        questionToPredict: Int
): Double {
    // QuestionID, (Category, Normalised value)
    val normalisedQuestions: Map<Int, Map<String, Double>> = allQuestions.map {
        Pair(it.questionId!!, normalizeCategories(it.categories.toTypedArray()))
    }.toMap()

    // QuestionID, Integer( -1 answered correctly, 0 not answered, 1 answered wrong)
    val answeredQuestions: Map<Int, Int> = answeredQuestions.map { Pair(it.questionId!!, it.resultWasGood.toInteger()) }.toMap()

    // Category, PersonalScore
    val userProfile = generateUserProfile(normalisedQuestions, answeredQuestions)

    // Category, IDF score
    val idfMap = calculateIDF(allQuestions)

    return makePrediction(normalisedQuestions[questionToPredict]!!, userProfile, idfMap)
}

private fun makePrediction(question: Map<String, Double>, userProfile: Map<String, Double>, idf: Map<String, Double>): Double {
    val categoryPredictions: ArrayList<Double> = ArrayList()
    for (category: Map.Entry<String, Double> in question) {
        categoryPredictions.add(category.value
                .times(userProfile.getOrDefault(category.key, 0.0))
                .times(idf.getOrDefault(category.key, 0.0))
        )
    }
    return categoryPredictions.sum()
}

private fun calculateIDF(allQuestions: Array<Question>): Map<String, Double> {
    val categoryCount: MutableMap<String, Int> = HashMap()
    val categoryIDF: MutableMap<String, Double> = HashMap()
    for (question: Question in allQuestions) {
        for (category: String in question.categories) {
            categoryCount[category] = categoryCount.getOrDefault(category, 0) + 1
        }
    }

    for (category: MutableMap.MutableEntry<String, Int> in categoryCount) {
        categoryIDF[category.key] = log10(1.0 + (allQuestions.size / category.value))
    }

    return categoryIDF.toMap()
}

private fun generateUserProfile(
        normalisedQuestions: Map<Int, Map<String, Double>>,
        answeredQuestions: Map<Int, Int>
): Map<String, Double> {
    val categoryScores: MutableMap<String, Double> = HashMap()
    for (answeredQuestion: Map.Entry<Int, Int> in answeredQuestions) {
        for (category: Pair<String, Double> in normalisedQuestions[answeredQuestion.key]!!.toList()) {
            val categoryAnswerProduct: Double = category.second * answeredQuestion.value
            categoryScores[category.first] = categoryAnswerProduct.plus(
                    categoryScores.getOrDefault(category.first, 0.0))
        }
    }
    return categoryScores.toMap()
}

private fun normalizeCategories(categories: Array<String>): Map<String, Double> {
    val normalizedNumber: Double = normalizeNumber(categories.size.toDouble())
    return categories.map { Pair(it, normalizedNumber) }.toMap()
}

private fun normalizeNumber(number: Double): Double = 1 / sqrt(number)
