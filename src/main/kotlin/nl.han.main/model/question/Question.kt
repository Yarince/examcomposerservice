package nl.han.main.model.question

abstract class Question (
        val questionId: Int,
        val questionText: String? = null,
        val questionType: QuestionType? = null,
        val subQuestions: Array<Question>? = null
        )