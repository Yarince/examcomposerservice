package nl.han.ica.examplatform.models.question

import java.util.*

class AnsweredQuestion(
        val questionId: Int,
        val resultWasGood: Boolean,
        val questionText: String,
        val categories: Array<String>,
        val answeredOn: Date,
        val questionType: String,
        val answerType: String
)