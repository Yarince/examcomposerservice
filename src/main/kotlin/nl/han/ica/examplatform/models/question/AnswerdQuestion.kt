package nl.han.ica.examplatform.models.question

import java.util.*


class AnswerdQuestion(
        questionId: Int?,
        questionOrderInExam: Int?,
        questionOrderText: String?,
        questionType: String,
        questionText: String?,
        questionPoints: Int?,
        courseId: Int,
        examType: String,
        answerType: String,
        answerTypePluginVersion: String,
        pluginVersion: String,
        categories: ArrayList<String>,
        subQuestions: ArrayList<Question>?,
        val resultWasGood: Boolean,
        val answeredOn: Date
) : Question(
        questionId,
        questionOrderInExam,
        questionOrderText,
        questionType,
        questionText,
        questionPoints,
        courseId,
        examType,
        answerType,
        answerTypePluginVersion,
        pluginVersion,
        categories,
        subQuestions
)