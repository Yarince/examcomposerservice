package nl.han.ica.examplatform.business.exam.practice

import nl.han.ica.examplatform.business.exam.practice.models.QuestionResult

internal fun categoriesWithRelevancePercentages(studentNr: Int, results: ArrayList<PracticeExamResult>, categories: ArrayList<String>): List<Pair<String, Double>> {
    val examRelevance: ArrayList<Pair<Int, Double>> = getExamRelevance(studentNr, results)

    val mapOfCategoriesAndTheirRelevancePercentages = mutableMapOf<String, Double>()
    for (result in results) {
        if (result.studentNr != studentNr) break
        for (category in categories) {
            val toetsVragen: List<QuestionResult> = result.questions.filter { it.categories.contains(category) }
            if (toetsVragen.isEmpty())
                break

            val percentageGoodQuestions = toetsVragen.map { q -> if (q.wasCorrect!!) 0.0 else 100.0 }.reduce { acc, i -> acc + i } / toetsVragen.size
            val huidigeToetsPercentage = examRelevance.find { it.first == result.examId }
            val reducedPercentage = (percentageGoodQuestions * huidigeToetsPercentage!!.second) / 100
            if (mapOfCategoriesAndTheirRelevancePercentages.containsKey(category)) {
                mapOfCategoriesAndTheirRelevancePercentages[category] = mapOfCategoriesAndTheirRelevancePercentages[category]!! + reducedPercentage
            } else {
                mapOfCategoriesAndTheirRelevancePercentages[category] = reducedPercentage
            }
        }
    }

    mapOfCategoriesAndTheirRelevancePercentages.forEach { if (it.value == 0.0) mapOfCategoriesAndTheirRelevancePercentages[it.key] = 10.0 }

    val sorted = mapOfCategoriesAndTheirRelevancePercentages.toList().sortedBy { it.second }
    val multiplier = 100 / sorted.last().second

    return sorted.map { Pair(it.first, it.second * multiplier) }
}