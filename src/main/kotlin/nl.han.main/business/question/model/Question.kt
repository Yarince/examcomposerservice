package nl.han.main.business.question.model

data class Question (
        val questionId: Int,
        val questionType: QuestionType? = null,
        val questionText: String? = null,
        val subQuestions: Array<Question>? = null
        )