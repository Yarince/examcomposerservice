package poc

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.FileReader
import java.util.*
import kotlin.collections.ArrayList
import java.util.Random
import kotlin.math.pow

const val MAGIC_NUMBER = 10.0

const val EXAM_SIZE = 10

fun main(args: Array<String>) {
//        testData()
    generatePracticeExam(1, 1).forEach(::println)
}

fun generatePracticeExam(courseId: Int, studentNumber: Int): ArrayList<Question> {
    val arrayOfExamResults = loadAssessedQuestions(courseId, studentNumber)
            ?: return randomizeQuestions(courseId)

    val questionTypePercentageList = calculateQuestionTypeRelevance(courseId, arrayOfExamResults)

    println(Gson().toJson(questionTypePercentageList))

    return ArrayList()
}


private fun calculateQuestionTypeRelevance(courseId: Int, examResults: ArrayList<ExamResult>): ArrayList<QuestionTypePercentage> {

    val weightedExams = getWeightedExams(examResults)
    val questionTypePercentageList = createQuestionTypePercentageList(courseId)

    // Loop through all available questionTypes for this course
    questionTypePercentageList.forEach questionTypes@{ questionTypePercentage: QuestionTypePercentage ->
        var perfectScore = true

        weightedExams.forEach examResult@{ exam: WeightedExam ->
            // If the questionType is already imperfect skip it
            if (!perfectScore) return@examResult

            // Get all questions for the current selected questionType
            val reviewedQuestions = exam.groupedQuestions[questionTypePercentage.questionType]
                    ?: return@examResult

            // If there is a result with more than 0 ```resultWasGood == false``` set perfectScore to false
            perfectScore = reviewedQuestions.filter { !it.resultWasGood }.count() == 0
        }

        if (perfectScore)
            questionTypePercentage.percentage = MAGIC_NUMBER
        else {
            // TODO: Assign score with Content-based filtering

            // TODO: Combine both percentages
            val relevance = 0.0

            // The total amount of questions made by student for a course
            val totalAmountQuestions = weightedExams.sumByDouble { it.groupedQuestions.values.sumByDouble { it.size.toDouble() } }

            // The amount of questions with this QuestionType
            val questionTypeInExams = weightedExams.sumBy {
                (it.groupedQuestions[questionTypePercentage.questionType] ?: return@sumBy 0).size
            }
            // The percentage questionTypes appeared in all exams
            val questionTypeAppearance = questionTypeInExams.div(totalAmountQuestions)
            // The amount of questions with current question type correctly answered
            val correct = weightedExams.sumByDouble {
                (it.groupedQuestions[questionTypePercentage.questionType]
                        ?: return@sumByDouble 0.0).filter { it.resultWasGood }.size.toDouble()
            }
            // The amount of questions with current question type incorrectly answered
            val incorrect = questionTypeInExams - correct

            println(totalAmountQuestions)

            println(questionTypePercentage.questionType)
            println(questionTypeAppearance)
            println(questionTypeInExams)
            println(correct)
            println(incorrect)
            println()

            questionTypePercentage.percentage = relevance
        }
    }

    return questionTypePercentageList
}

private fun createQuestionTypePercentageList(courseId: Int): ArrayList<QuestionTypePercentage> {
    // Fill questionTypePercentageList with all available questionTypes
    val questionTypePercentageList = ArrayList<QuestionTypePercentage>()
    loadQuestionTypes(courseId).forEach { questionTypePercentageList.add(QuestionTypePercentage(it.questionType)) }
    return questionTypePercentageList
}

private fun getWeightedExams(arrayOfExamResults: ArrayList<ExamResult>): ArrayList<WeightedExam> {
    val weightedExams = ArrayList<WeightedExam>()
    var current = 0

    arrayOfExamResults.forEach {
        weightedExams.add(WeightedExam(
                it.examId,
                2.0.pow(current),
                (it.questions ?: return@forEach).groupBy({ it.questionType }, { it })
        ))
        current++
    }

    weightedExams.sortBy { it.examId }

    return weightedExams

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

internal fun loadQuestionTypes(courseId: Int): ArrayList<QuestionTypePercentage> =
        arrayListOf(
                QuestionTypePercentage("OpenQuestion"),
                QuestionTypePercentage("MultipleChoiceQuestion"),
                QuestionTypePercentage("TreeQuestion"),
                QuestionTypePercentage("GraphQuestion"),
                QuestionTypePercentage("DrawQuestion"),
                QuestionTypePercentage("BigOhQuestion")
        )


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
                    questionId = questionId,
                    resultWasGood = rnd.nextBoolean(),
                    questionText = text.shuffled().take(1)[0],
                    categories = arrayListOf(category.shuffled().take(1)[0], category.shuffled().take(1)[0]),
                    questionType = questionType.shuffled().take(1)[0]
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
