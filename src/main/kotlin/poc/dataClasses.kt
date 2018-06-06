package poc

import java.util.*


data class AnsweredQuestion(
        val questionId: Int,
        val questionText: String,
        val categories: ArrayList<String>,
        val questionType: String,
        val answeredOn: Date
)

data class ExamResult(
        var weight: Double? = 0.0,
        val examId: Int,
        val examDate: Date,
        var questions: ArrayList<ReviewedQuestion>?,
        var groupedQuestions: Map<String, List<ReviewedQuestion>>? = null
)

data class ReviewedQuestion(
        val questionId: Int,
        val resultWasGood: Boolean,
        val questionText: String,
        val categories: ArrayList<String>,
        val questionType: String
)

data class QuestionTypePercentage(
        val questionType: String,
        val percentage: Double
)

data class Question(
        val questionId: Int,
        val questionText: String,
        val questionType: String
)