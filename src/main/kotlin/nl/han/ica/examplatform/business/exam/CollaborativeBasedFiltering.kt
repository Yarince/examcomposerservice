package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.models.question.AnswerdQuestion
import kotlin.math.pow
import kotlin.math.sqrt

fun collaborativeFiltering(
        answeredQuestionsStudent: Array<AnswerdQuestion>,
        answeredQuestionsOtherStudents: Map<Int, Map<Int, AnswerdQuestion>>,
        idQuestionToPredict: Int
): Double {
    // If a question has not bean answered by anyone make it top priority
    return if (answeredQuestionsOtherStudents.containsKey(idQuestionToPredict)) {
        answeredQuestionsOtherStudents.map { it: Map.Entry<Int, Map<Int, AnswerdQuestion>> ->
            calculateRelevance(answeredQuestionsStudent, it.value.values.toTypedArray()) *
                    it.value.getValue(idQuestionToPredict).resultWasGood.toInteger()
        }.average()
    } else 1.0
}

private fun calculateRelevance(answeredQuestionsStudent1: Array<AnswerdQuestion>, answeredQuestionsStudent2: Array<AnswerdQuestion>): Double {
    val questionMapStudent1: Map<Int, Pair<Double, Double>> =
            calculateQuestionAnswersIntegers(answeredQuestionsStudent1)
    val questionMapStudent2: Map<Int, Pair<Double, Double>> =
            calculateQuestionAnswersIntegers(answeredQuestionsStudent2)

    val sumQuestionMapStud1: Double = questionMapStudent1.map { it.value.second }.sum()
    val sumQuestionMapStud2: Double = questionMapStudent2.map { it.value.second }.sum()
    val productOfStud1AndStud2: Double = calculateProductOfStudent1And2(
            questionMapStudent1.map { Pair(it.key, it.value.first) }.toMap(),
            questionMapStudent2.map { Pair(it.key, it.value.first) }.toMap())

    return productOfStud1AndStud2.div(sqrt(sumQuestionMapStud1 * sumQuestionMapStud2))
}

private fun calculateProductOfStudent1And2(stud1: Map<Int, Double>, stud2: Map<Int, Double>): Double {
    val resultMap: MutableMap<Int, Double> = HashMap()
    for (calculate: Map.Entry<Int, Double> in stud1)
        resultMap[calculate.key] = calculate.value * stud2.getOrDefault(calculate.key, 0.0)
    return resultMap.map { it.value }.sum()
}

private fun calculateQuestionAnswersIntegers(answeredQuestions: Array<AnswerdQuestion>): Map<Int, Pair<Double, Double>> {
    val questionMap: MutableMap<Int, Int> = HashMap()
    for (answeredQuestion: AnswerdQuestion in answeredQuestions)
        questionMap[answeredQuestion.questionId!!] = answeredQuestion.resultWasGood.toInteger()

    val avg: Double = questionMap.values.average()
    val resultMap: MutableMap<Int, Pair<Double, Double>> = HashMap()

    for (question: MutableMap.MutableEntry<Int, Int> in questionMap)
        resultMap[question.key] = Pair(question.value - avg, (question.value - avg).pow(2))

    return resultMap.toMap()
}
