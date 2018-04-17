package nl.han.main.model.question

abstract class Question (
        val questionId: Int,
        val questionText: String? = null,
        val subQuestions: Array<Question>? = null
        ) {
val questionType = QuestionType.OPEN_QUESTION
}