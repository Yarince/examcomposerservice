package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.controllers.DatabaseException
import nl.han.ica.examplatform.models.exam.PracticeExam
import nl.han.ica.examplatform.models.question.AnswerdQuestion
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import java.time.LocalDateTime

fun main(args: Array<String>) {
    val practiceExam: PracticeExam = GeneratePersonalPracticeExam(QuestionDAO()).generatePracticeExam(200000, 425)
    print("Debug")
}

class GeneratePersonalPracticeExam(val QuestionDAO: QuestionDAO) {

    fun generatePracticeExam(studentNumber: Int, courseId: Int): PracticeExam {

        val allQuestionsFromCourse: Array<Question> = try {
            QuestionDAO.getAllQuestionsFromCourse(courseId)
        } catch (e: DatabaseException) {
            // Add Logger
            throw e
        }

        val answeredQuestionsByRelevantOthers: Map<Int, Map<Int, AnswerdQuestion>> = try {
            QuestionDAO.getAnsweredQuestionsByRelevantOthers(studentNumber)
        } catch (e: DatabaseException) {
            // Add Logger
            throw e
        }

        val answeredQuestions: Array<AnswerdQuestion> = try {
            QuestionDAO.getAllAnsweredQuestionsByStudent(studentNumber)
        } catch (e: DatabaseException) {
            // Add Logger
            throw e
        }

        // QuestionID, Relevance
        val questionsRelevance: Map<Int, Double> = calculateRelevanceForQuestions(
                allQuestionsFromCourse, answeredQuestions, answeredQuestionsByRelevantOthers)
                .toList().sortedBy { (_, value) -> value }.asReversed().toMap()

        val questionsMap: Map<Int, Question> = allQuestionsFromCourse.map { Pair(it.questionId!!, it) }.toMap()
        val examQuestions: ArrayList<Question> = ArrayList()

        var questionOrderInExam = 1
        for (questionID: Int in questionsRelevance.keys) {
            val question: Question = questionsMap[questionID]!!
            question.questionOrderInExam = questionOrderInExam
            examQuestions.add(question)
            questionOrderInExam++
            if (examQuestions.size >= 10) {
                break
            }
        }

        return PracticeExam("Practice Exam (${LocalDateTime.now()})", courseId, examQuestions.toTypedArray())
    }

    private fun calculateRelevanceForQuestions(
            allQuestionsFromCourse: Array<Question>,
            answeredQuestions: Array<AnswerdQuestion>,
            answeredQuestionsByRelevantOthers: Map<Int, Map<Int, AnswerdQuestion>>
    ): Map<Int, Double> {
        return allQuestionsFromCourse.map {
            Pair(it.questionId!!, arrayOf(
                    contentBasedFiltering(allQuestionsFromCourse, answeredQuestions, it.questionId),
                    collaborativeFiltering(answeredQuestions, answeredQuestionsByRelevantOthers, it.questionId)
            ).average())
        }.toMap()
    }
}

internal fun Boolean.toInteger() = if (this) 1 else -1
