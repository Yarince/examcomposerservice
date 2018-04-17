package nl.han.main.model.question

class OpenQuestion(
        questionId: Int,
        questionText: String?,
        course: String,
        subId: String,
        examType: ExamType,
        subQuestions: Array<Question>?) :
        Question(questionId,
                questionText,
                QuestionType.OPEN_QUESTION, // Hard coded a open question is always an OPEN_QUESTION,
                course,
                subId,
                examType,
                subQuestions)