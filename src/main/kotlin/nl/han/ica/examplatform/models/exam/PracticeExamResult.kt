package nl.han.ica.examplatform.models.exam

import nl.han.ica.examplatform.models.question.QuestionResult

data class PracticeExamResult(val examId: Int, val studentNr: Int, val questions: ArrayList<QuestionResult>)
