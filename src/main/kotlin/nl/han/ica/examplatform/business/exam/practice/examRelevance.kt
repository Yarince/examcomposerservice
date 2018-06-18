package nl.han.ica.examplatform.business.exam.practice


import nl.han.ica.examplatform.business.exam.practice.models.QuestionResult
import kotlin.math.pow

data class PracticeExamResult(val examId: Int, val studentNr: Int, val questions: ArrayList<QuestionResult>)

internal fun getExamRelevance(studentNr: Int, results: ArrayList<PracticeExamResult>): ArrayList<Pair<Int, Double>> {
    //return results.map { r -> Pair(r.examId, calculateRelevanceOfPracticeExam(results.size, )) }
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

internal fun amountOfPracticeExamsOfAStudent(studentNr: Int, data: ArrayList<PracticeExamResult>): Int {
    var total = 0
    for (oefentoetsen in data) {
        if (oefentoetsen.studentNr == studentNr) {
            total++
        }
    }
    return total
}