package nl.han.main.model.question

class OpenQuestion(
        questionId: Int,
        questionText: String?,
        subQuestions: Array<Question>?) :
        Question(questionId,
                questionText,
                QuestionType.OPEN_QUESTION, // Hard coded a open question is always an OPEN_QUESTION
                subQuestions)