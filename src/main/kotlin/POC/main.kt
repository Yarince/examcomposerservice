package POC

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.FileReader
import kotlin.math.pow


fun main(args: Array<String>) {
}

internal fun categoriesWithRelevancePercentages(studentNr: Int) : ArrayList<Pair<String, Double>> {
    val results: ArrayList<Results> = loadQuestions().toCollection(arrayListOf())
    val dataPairs: ArrayList<Pair<Int, Double>> = fetchStudentPracticeExamsWithTheirRelevancePercentages(studentNr)
    dataPairs.forEach { println(it) }

    val categories = results.map { r -> r.questions.map { q -> q.categories }.reduce { acc, list -> acc.plus(list) } }.reduce { acc, list -> acc.plus(list) }.distinct()

    val mapOfCategoriesAndTheirRelevancePercentages = mutableMapOf<String, Double>()
    for (result in results) {
        if (result.studentNr != studentNr) break
        for (category in categories) {
            val toetsVragen = result.questions.filter { it.categories.contains(category) }
            if (toetsVragen.isEmpty())
                break

            val practiceExamQuestionsGoodOrFalse = toetsVragen.map { q -> if (q.resultWasGood) 0.0 else 100.0 }.reduce { acc, i -> acc + i }
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

    return ArrayList(toListOfMaps(mapOfCategoriesAndTheirRelevancePercentages))
}

fun toListOfMaps(map: Map<String, Double>): List<Pair<String, Double>> {
    return map.map { (number, answer) ->
        Pair(number, answer)
    }
}

internal fun loadQuestions(): Array<Results> {
    // Here the db should do some stuff in the real implementation
    val reader = JsonReader(FileReader("src/main/kotlin/poc/resources/data.json"))
    return Gson().fromJson(reader, Array<Results>::class.java)
}

data class Results(val examId: Int, val studentNr: Int, val questions: Array<QuestionResult>)
data class QuestionResult(val questionId: Int, val categories: Array<String>, val resultWasGood: Boolean)

//TODO add Questions ArrayList
data class PracticeExam(val examId: Int, val studentNr: Int, val name: String, val courseId: Int)

//=========================================================================================================================================================================

//Step 1
internal fun checkIfStudentCompletedOtherPracticeExams(studentNr: Int): Boolean {
    var data: ArrayList<Results> = loadQuestions().toCollection(arrayListOf())
    for (examens in data) {
        if (examens.studentNr == studentNr)
            return true
    }
    return false
}
//=========================================================================================================================================================================

// Step 2
internal fun fetchStudentPracticeExamsWithTheirRelevancePercentages(studentNr: Int): ArrayList<Pair<Int, Double>> {
    var data: ArrayList<Results> = loadQuestions().toCollection(arrayListOf())
    var practiceExamsOfAStudentPairedWithTheirWeightingAscending: ArrayList<Pair<Int, Double>> = ArrayList()
    for (oefentoetsen in data) {
        if (oefentoetsen.studentNr == studentNr) {
            practiceExamsOfAStudentPairedWithTheirWeightingAscending.add(Pair(oefentoetsen.examId, calculateRelevanceOfPracticeExam(amountOfPracticeExamsOfAStudent(oefentoetsen.studentNr, data), data.indexOf(oefentoetsen) + 1)))
        }
    }
    return practiceExamsOfAStudentPairedWithTheirWeightingAscending
}

internal tailrec fun recurPow(n: Int, iterator: Int = 0, total: Double = 0.0): Double {
    if (iterator >= n) return total
    return recurPow(n, iterator + 1, total + 2.0.pow(iterator))
}

internal tailrec fun recurMultiplication(n: Int, iterator: Int = 1, total: Int = 1): Int {
    if (iterator >= n) return total
    return recurMultiplication(n, iterator + 1, total * 2)
}

internal fun calculateRelevanceOfPracticeExam(amountOfPracticeExams: Int, importanceRanking: Int): Double {
    return 100 / (recurPow(amountOfPracticeExams)) * recurMultiplication(importanceRanking)
}

internal fun amountOfPracticeExamsOfAStudent(studentNr: Int, data: ArrayList<Results>): Int {
    var total = 0
    for (oefentoetsen in data) {
        if (oefentoetsen.studentNr == studentNr) {
            total++
        }
    }
    return total
}