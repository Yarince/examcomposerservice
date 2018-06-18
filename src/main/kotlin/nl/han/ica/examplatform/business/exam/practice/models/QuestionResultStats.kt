package nl.han.ica.examplatform.business.exam.practice.models

data class QuestionResultStats(val questionId: Int, val nResults: Int, val nGood: Int, val nWrong: Int)
