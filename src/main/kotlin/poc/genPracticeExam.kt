package poc

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import nl.han.ica.examplatform.business.exam.generatePracticeExam
import nl.han.ica.examplatform.models.exam.PracticeExam
import nl.han.ica.examplatform.persistence.category.CategoryDAO
import nl.han.ica.examplatform.persistence.category.ICategoryDAO
import nl.han.ica.examplatform.persistence.question.IQuestionDAO
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import java.io.FileReader
import java.util.*
import kotlin.collections.ArrayList
import java.util.Random
import kotlin.math.pow

// Nothing to see here. No explanation needed!
const val MAGIC_NUMBER = 10.0

fun main(args: Array<String>) {
    generateIndividualPracticeExam(1, 1, QuestionDAO(), CategoryDAO())
}

fun generateIndividualPracticeExam(courseId: Int, studentNr: Int, questionDAO: IQuestionDAO, categoryDAO: ICategoryDAO): PracticeExam {

    // Load earlier results. If there are no results generate a random practice exam.
    val arrayOfExamResults = loadAssessedQuestions(courseId, studentNr)
            ?: return generatePracticeExam(courseId, studentNr, questionDAO, categoryDAO)

    // Calculate the relevance per questionType
    val questionTypePercentageList = calculateQuestionTypeRelevance(courseId, arrayOfExamResults)

    // Temporarily print the results of the percentage calculation.
    println(Gson().toJson(questionTypePercentageList))

    return PracticeExam("Toets", courseId, ArrayList())
}


private fun calculateQuestionTypeRelevance(courseId: Int, examResults: ArrayList<ExamResult>): ArrayList<QuestionTypePercentage> {

    val weightedExams = getWeightedExams(examResults)
    val questionTypePercentageList = getQuestionTypeList(courseId)

    // Loop through all available questionTypes for this course
    questionTypePercentageList.forEach questionTypes@{ typePercentage: QuestionTypePercentage ->
        var perfectScore = true

        weightedExams.forEach examResult@{ exam: WeightedExam ->
            // If the questionType is already imperfect skip it
            if (!perfectScore) return@examResult

            // Get all questions for the current selected questionType
            val reviewedQuestions = exam.groupedQuestions[typePercentage.questionType]
                    ?: return@examResult

            // If there is a result with more than 0 resultWasGood == false set perfectScore to false
            perfectScore = reviewedQuestions.filter { !it.resultWasGood }.count() == 0
        }

        // If the questionType is perfectly answered a default percentage is assigned
        if (perfectScore) {
            typePercentage.percentage = MAGIC_NUMBER
            return@questionTypes
        } else {

            // Calculate the questionType relevance
            typePercentage.percentage = weightedExams.sumByDouble { exam: WeightedExam ->

                // Get all questions for the current questionTypes
                val questionsForType = (exam.groupedQuestions[typePercentage.questionType]
                        ?: return@sumByDouble 0.0)

                // Multiply the percentage of questions wrongly answered by the relevance of the exam
                return@sumByDouble exam.weight * (questionsForType.filter { !it.resultWasGood }
                        .size.toDouble() / questionsForType.size)
            }
                    // Divide the relevance by the total amount of points to be distributed.
                    // Multiply by 100 to get correct percentage.
                    .div(weightedExams.sumByDouble { it.weight }) * 100
        }
    }

    return questionTypePercentageList
}

private fun getQuestionTypeList(courseId: Int): ArrayList<QuestionTypePercentage> {
    // Fill questionTypePercentageList with all available questionTypes
    val questionTypePercentageList = ArrayList<QuestionTypePercentage>()
    loadQuestionTypes(courseId).forEach { questionTypePercentageList.add(QuestionTypePercentage(it.questionType)) }
    return questionTypePercentageList
}

private fun getWeightedExams(arrayOfExamResults: ArrayList<ExamResult>): List<WeightedExam> {

    // Calculate total points in the exams
    val totalPoints = arrayOfExamResults
            .mapIndexed { current, _ -> 2.0.pow(current) }
            .sum()

    // Make a list of Weighted exams grouped review questions
    return arrayOfExamResults.withIndex().map { (current, it) ->
        WeightedExam(
                it.examId,
                100 / totalPoints * 2.0.pow(current),
                it.questions.groupBy({ it.questionType }, { it })
        )
    }
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
