package poc

internal fun categoriesWithRelevancePercentages(studentNr: Int, results: ArrayList<Results>): List<Pair<String, Double>> {
    val dataPairs: ArrayList<Pair<Int, Double>> = fetchStudentPracticeExamsWithTheirRelevancePercentages(studentNr, results)

    val categories = results.map { r -> r.questions.map { q -> q.categories }.reduce { acc, list -> acc.plus(list) } }.reduce { acc, list -> acc.plus(list) }.distinct()

    val mapOfCategoriesAndTheirRelevancePercentages = mutableMapOf<String, Double>()
    for (result in results) {
        if (result.studentNr != studentNr) break
        for (category in categories) {
            val toetsVragen = result.questions.filter { it.categories.contains(category) }
            if (toetsVragen.isEmpty())
                break

            val practiceExamQuestionsGoodOrFalse = toetsVragen.map { q -> if (q.wasCorrect!!) 0.0 else 100.0 }.reduce { acc, i -> acc + i }
            val percentageGoodQuestions = practiceExamQuestionsGoodOrFalse / toetsVragen.size
            val huidigeToetsPercentage = dataPairs.find { it.first == result.examId }
            val reducedPercentage = (percentageGoodQuestions * huidigeToetsPercentage!!.second) / 100
            if (mapOfCategoriesAndTheirRelevancePercentages.containsKey(category)) {
                mapOfCategoriesAndTheirRelevancePercentages[category] = mapOfCategoriesAndTheirRelevancePercentages[category]!! + reducedPercentage
            } else {
                mapOfCategoriesAndTheirRelevancePercentages[category] = reducedPercentage
            }
        }
    }

    mapOfCategoriesAndTheirRelevancePercentages.forEach { if (it.value == 0.0) mapOfCategoriesAndTheirRelevancePercentages[it.key] = 10.0 }
    var sorted = mapOfCategoriesAndTheirRelevancePercentages.toList().sortedBy { it.second }

    if (sorted.last().second <= 50.0) {
        val multiplier = 100 / sorted.last().second
        sorted = sorted.map { Pair(it.first, it.second * multiplier) }
    }

    return sorted
}