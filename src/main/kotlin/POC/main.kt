package POC

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.FileReader
import kotlin.math.pow


fun main(args : Array<String>) {
    var everythingToArrayList: ArrayList<Results> = loadQuestions().toCollection(arrayListOf())
    var questionResult: ArrayList<QuestionResult> = everythingToArrayList[0].questions.toCollection(ArrayList())
    var dataPairs: ArrayList<Pair<Int, Double>> = fetchStudentPracticeExamsWithTheirRelevancePercentages(123)


    println(categoriesByExamId(1, everythingToArrayList))
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
internal fun checkIfStudentCompletedOtherPracticeExams(studentNr: Int) : Boolean {
    var data: ArrayList<Results> = loadQuestions().toCollection(arrayListOf())
    for(examens in data) {
        if(examens.studentNr == studentNr)
            return true
    }
    return false
}
//=========================================================================================================================================================================

// Step 2
internal fun fetchStudentPracticeExamsWithTheirRelevancePercentages(studentNr: Int) : ArrayList<Pair<Int, Double>> {
    var data: ArrayList<Results> = loadQuestions().toCollection(arrayListOf())
    var practiceExamsOfAStudentPairedWithTheirWeightingAscending : ArrayList<Pair<Int, Double>> = ArrayList()
    for (oefentoetsen in data) {
        if (oefentoetsen.studentNr == studentNr) {
            practiceExamsOfAStudentPairedWithTheirWeightingAscending.add(Pair(oefentoetsen.examId, calculateRelevanceOfPracticeExam(amountOfPracticeExamsOfAStudent(oefentoetsen.studentNr, data), data.indexOf(oefentoetsen)+1)))
        }
    }
        return practiceExamsOfAStudentPairedWithTheirWeightingAscending
}

internal tailrec fun recurPow(n: Int, iterator: Int = 0, total: Double = 0.0): Double {
    if (iterator >= n) return total
    return recurPow(n, iterator+1 , total + 2.0.pow(iterator))
}

internal tailrec fun recurMultiplication(n: Int, iterator: Int = 1, total: Int=1) : Int {
    if (iterator >= n) return total
    return recurMultiplication(n, iterator+1, total*2)
}

internal fun calculateRelevanceOfPracticeExam(amountOfPracticeExams: Int, importanceRanking: Int) : Double {
    return 100/(recurPow(amountOfPracticeExams)) * recurMultiplication(importanceRanking)
}

internal fun amountOfPracticeExamsOfAStudent(studentNr: Int, data: ArrayList<Results>) : Int {
    var total = 0
    for (oefentoetsen in data) {
        if (oefentoetsen.studentNr == studentNr) {
           total++
        }
    }
    return total
}

//=========================================================================================================================================================================

// Step 3

internal fun categoryResultsPerPastPracticeExam(examResults: ArrayList<Results>) {
    var categoriesWithTheirPercentageCorrectAnswered: ArrayList<Triple<Int, String, Double>> = ArrayList()
    var counterGoed = 0
    var counterFout = 0



    for(resultaten in examResults) {
        //categoriesWithTheirPercentageCorrectAnswered.add((Triple(resultaten.examId, resultaten.questions[counterFout+counterGoed].categories.)))
    }

}

internal fun categoriesByExamId(examid: Int): ArrayList<Pair<Int, ArrayList<String>>> {
    var categories: ArrayList<Pair<Int, ArrayList<String>>> = ArrayList()
    var results: ArrayList<Results> = loadQuestions().toCollection(ArrayList())

    for(i in results.withIndex()) {
        if(i.value.examId==examid) {
            for (j in i.value.questions.withIndex()) {
                categories.add(Pair(examid, getCategoriesOutOfQuestions(results[i.index].questions.toCollection(ArrayList()))))
            }
        }
    }
    return categories.distinct().toCollection(ArrayList())
}

internal fun getCategoriesOutOfQuestions(questions: ArrayList<QuestionResult>) : ArrayList<String> {
    var categories: ArrayList<String> = ArrayList()

    for((i, question) in questions.withIndex()) {
        for (category in question.categories.withIndex()) {
            categories.add(category.value)
        }
    }
    return categories.distinct().toCollection(ArrayList())
}

internal fun calculatePercentageGoodAnswered(examId: Int, categoriesInExam: ArrayList<String>, results: ArrayList<Results>) : ArrayList<Double> {
    var cGoed = 0.0
    var cTotalCategoryQuestions = 0.0
    var cTotalQuestions = 0.0
    var correctPercentage: Double
    var correctPercentages: ArrayList<Double> = ArrayList()

    for (result in results) {
        if (result.examId == examId) {
            //println(result)
        }
    }
    return correctPercentages
}

internal fun berekenPercentageGoedCategorieInToets(examId: Int, results: ArrayList<QuestionResult>): ArrayList<String> {
    var cGoedBeantwoordt = 0.0
    var categoryList = getCategoriesOutOfQuestions(results)



    return categoryList
}

internal fun percentageFalseCategories(studentNr: Int, studentResults: ArrayList<Results>): MutableMap<Int, Pair<String, Double>>? {
    var examsWithTheirRelevancePercentages: ArrayList<Pair<Int, Double>> = fetchStudentPracticeExamsWithTheirRelevancePercentages(studentNr)
    var categoriesOfExams: ArrayList<Pair<Int, ArrayList<String>>> = ArrayList()


    return null
}

internal fun percentageWrongAnsweredQuestions(studentNr: Int) {
    var studentPracticeExamResultData: ArrayList<Results> = loadQuestions().toCollection(ArrayList())
    var mapWithExamsAndCategoriesWrongAnswered: MutableMap<Int, Pair<String, Double>>
    var categoriesPerExam: ArrayList<Pair<Int, ArrayList<String>>> = categoriesByExamId(1)

    for(data in studentPracticeExamResultData) {
        if(data.studentNr==studentNr) {
            for(questions in data.questions) {
                for(categories in questions.categories) {

                }
            }
        }
    }


}