package nl.han.ica.examplatform.models.exam

import nl.han.ica.examplatform.models.question.AnsweredQuestion
import java.util.*

class AnsweredExam (
        val examId: Int,
        val examDate: Date,
        val answeredQuestions: Array<AnsweredQuestion>
)