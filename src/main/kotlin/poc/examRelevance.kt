package poc

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import poc.models.Question
import java.io.FileReader
import kotlin.math.pow

internal fun loadQuestions(): Array<Results> {
    // Here the db should do some stuff in the real implementation
    val reader = JsonReader(FileReader("src/main/kotlin/poc/resources/assessedExams.json"))
    return Gson().fromJson(reader, Array<Results>::class.java)
}

data class Results(val examId: Int, val studentNr: Int, val questions: Array<Question>)
data class QuestionResult(val questionId: Int, val categories: Array<String>, val resultWasGood: Boolean)
//TODO add Questions ArrayList
data class PracticeExam(val examId: Int, val studentNr: Int, val name: String, val courseId: Int)

//=========================================================================================================================================================================

//Step 1
internal fun checkIfStudentCompletedOtherPracticeExams(studentNr: Int): Boolean {
    val data: ArrayList<Results> = loadQuestions().toCollection(arrayListOf())
    for (examens in data) {
        if (examens.studentNr == studentNr)
            return true
    }
    return false
}
//=========================================================================================================================================================================

// Step 2
internal fun fetchStudentPracticeExamsWithTheirRelevancePercentages(studentNr: Int, results: ArrayList<Results>): ArrayList<Pair<Int, Double>> {
    val practiceExamsOfAStudentPairedWithTheirWeightingAscending: ArrayList<Pair<Int, Double>> = ArrayList()
    for (oefentoetsen in results) {
        if (oefentoetsen.studentNr == studentNr) {
            practiceExamsOfAStudentPairedWithTheirWeightingAscending.add(Pair(oefentoetsen.examId, calculateRelevanceOfPracticeExam(amountOfPracticeExamsOfAStudent(oefentoetsen.studentNr, results), results.indexOf(oefentoetsen) + 1)))
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