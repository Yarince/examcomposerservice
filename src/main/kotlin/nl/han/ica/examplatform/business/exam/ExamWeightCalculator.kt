package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.models.question.AnsweredQuestion
import java.math.RoundingMode
import kotlin.math.pow

private const val DECIMALS: Int = 4

fun calculateWeightForExams(exams: Map<Int, Array<AnsweredQuestion>>): Map<Int, Double> {
    val powPointsArray: Array<Double> = (1..exams.size).map { 2.0.pow(it) }.toTypedArray()
    val powSum: Double = powPointsArray.sum()
    val powPercentageArray: Array<Double> = powPointsArray
            .map { (1 / powSum * it).toBigDecimal().setScale(DECIMALS, RoundingMode.UP).toDouble() }
            .filter { it >= 1 / 10.0.pow(DECIMALS - 1) }
            .toTypedArray()

    val questionMap: MutableMap<Int, MutableList<Double>> = HashMap()
    for ((index: Int, exam: Pair<Int, Array<AnsweredQuestion>>) in exams.toList().withIndex()) {
        for (answeredQuestion: AnsweredQuestion in exam.second) {
            val examFactors = questionMap.getOrDefault(answeredQuestion.questionId, ArrayList())
            examFactors.add(powPercentageArray.getOrElse(index, { _ -> 0.0 }))
            questionMap[answeredQuestion.questionId] = examFactors
        }
    }

    return questionMap.map { Pair(it.key, it.value.average() * -1) }.toMap()
}