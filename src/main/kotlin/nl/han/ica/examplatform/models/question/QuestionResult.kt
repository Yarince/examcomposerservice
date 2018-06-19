package nl.han.ica.examplatform.models.question

import java.util.*

data class QuestionResult(val questionId: Int, val questionText: String, val categories: ArrayList<String>, val type: String, val practiceTestResultId: Int? = null, val wasCorrect: Boolean? = null)
