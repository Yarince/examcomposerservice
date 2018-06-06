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
        val examId: Int,
        val examDate: Date,
        var questions: ArrayList<ReviewedQuestion>?
        )

data class WeightedExam(
        val examId: Int,
        val weight: Double,
        var groupedQuestions: Map<String, List<ReviewedQuestion>>
)

data class ReviewedQuestion(
        var weight: Double = 0.0,
        val questionId: Int,
        val resultWasGood: Boolean,
        val questionText: String,
        val categories: ArrayList<String>,
        val questionType: String
)

data class QuestionTypePercentage(
        val questionType: String,
        var percentage: Double = 0.0
)

data class Question(
        val questionId: Int,
        val questionText: String,
        val questionType: String
)