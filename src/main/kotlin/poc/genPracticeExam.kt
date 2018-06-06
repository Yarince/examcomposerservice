package poc

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.FileReader
import java.lang.Double.sum
import java.util.*
import kotlin.collections.ArrayList
import java.util.Random
import kotlin.math.pow


const val EXAM_SIZE: Int = 10

fun main(args: Array<String>) {
//        testData()
    generatePracticeExam(1, 1).forEach(::println)
}

fun generatePracticeExam(courseId: Int, studentNumber: Int): ArrayList<Question> {
    val arrayOfExamResults = loadAssessedQuestions(courseId, studentNumber)
            ?: return randomizeQuestions(courseId)

    val groupedByQuestionType = groupByQuestionType(arrayOfExamResults)

    setExamWeight(groupedByQuestionType)


    groupedByQuestionType.forEach {

    }


    var totalWeight = 0.0
    for (examResult in groupedByQuestionType) {
        totalWeight += sum(examResult.weight ?: continue, totalWeight)
    }

//    println(totalWeight)
    println(Gson().toJson(groupedByQuestionType))

    return ArrayList()
}

private fun setExamWeight(groupedByQuestionType: ArrayList<ExamResult>) {
    var current = 0
    groupedByQuestionType.forEach {
        it.weight = 2.0.pow(current)
        current++
    }
}

private fun groupByQuestionType(arrayOfExamResults: ArrayList<ExamResult>): ArrayList<ExamResult> {

    arrayOfExamResults.forEach {
        it.groupedQuestions = (it.questions ?: return@forEach).groupBy({ it.questionType }, { it })
        it.questions = null
    }

    arrayOfExamResults.sortBy { it.examId }

    return arrayOfExamResults
}

fun randomizeQuestions(courseId: Int): ArrayList<Question> {
    // Add randomized questions
    // TODO: Use proper algorithm
    val randomGenerator = Random()
    val questionPool = (loadQuestionPool(courseId)
            ?: throw Exception("No question pool")).toCollection(ArrayList())

    val exam = ArrayList<Question>()

    for (i in 0..EXAM_SIZE) {
        val pick = randomGenerator.nextInt(questionPool.size)
        exam.add(questionPool.removeAt(pick))
    }

    return exam
}


internal fun loadQuestionPool(courseId: Int): Array<Question>? {
    // Here the db should do some stuff in the real implementation
    val reader = JsonReader(FileReader("src/main/kotlin/poc/resources/questionBankNotAnswered.json"))
    return Gson().fromJson(reader, Array<Question>::class.java)
}

internal fun loadAnsweredQuestions(courseId: Int, studentNr: Int): ArrayList<AnsweredQuestion> {
    // Here the db should do some stuff in the real implementation
    val reader = JsonReader(FileReader("src/main/kotlin/poc/resources/questionsAnswered.json"))
    return Gson().fromJson<Array<AnsweredQuestion>>(reader, Array<AnsweredQuestion>::class.java).toCollection(ArrayList())
}

internal fun loadAssessedQuestions(courseId: Int, studentNr: Int): ArrayList<ExamResult>? {
    // Here the db should do some stuff in the real implementation
    val reader = JsonReader(FileReader("src/main/kotlin/poc/resources/question+result.json"))
    return Gson().fromJson<Array<ExamResult>>(reader, Array<ExamResult>::class.java).toCollection(ArrayList())
}

fun testData() {
    var questionId = 1
    var examId = 1
    val text = arrayListOf("Vraag over ASR", "Vraag over DCAR", "Vraag over ATAM", "Vraag over dingen")
    val category = arrayListOf("ASD", "Graph", "AVL", "Fietsen", "Bomen", "Trees", "Algorithms")
    val questionType = arrayListOf("OpenQuestion", "MultipleChoiceQuestion", "TreeQuestion", "GraphQuestion", "DrawQuestion", "BigOhQuestion")
    val rnd = Random()

    val exams = ArrayList<ExamResult>()
    for (i in 1..4) {
        val questions = ArrayList<ReviewedQuestion>()
        for (j in 1..8) {
            questions.add(ReviewedQuestion(
                    questionId,
                    rnd.nextBoolean(),
                    text.shuffled().take(1)[0],
                    arrayListOf(category.shuffled().take(1)[0], category.shuffled().take(1)[0]),
                    questionType.shuffled().take(1)[0]
            ))
            questionId++
        }
        exams.add(ExamResult(
                examId = examId,
                examDate = Date(Math.abs(System.currentTimeMillis() - rnd.nextLong())),
                questions = questions
        ))
        examId++
    }
    println(Gson().toJson(exams))


    val typePercentageList = ArrayList<QuestionTypePercentage>()
    for (type: String in questionType)
        typePercentageList.add(QuestionTypePercentage(type, Random().nextDouble() * 100))

    print(Gson().toJson(typePercentageList))

}
