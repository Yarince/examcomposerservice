package nl.han.ica.examplatform.business.exam

import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import nl.han.ica.examplatform.controllers.DatabaseException
import nl.han.ica.examplatform.models.exam.AnsweredExam
import nl.han.ica.examplatform.models.exam.PracticeExam
import nl.han.ica.examplatform.models.question.AnsweredQuestion
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import java.io.File
import java.io.FileReader
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.*
import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    for (i in 10..110) {

        val time: Long = System.currentTimeMillis()
        val practiceExam: PracticeExam = GeneratePersonalPracticeExam(QuestionDAO()).generatePracticeExam(200000, 425)
        println("$i - ${System.currentTimeMillis() - time} Millis")
        println("$i - Memory after generating exam: ${Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()}")

        val gson = GsonBuilder().setPrettyPrinting().create()
        val reader1 = JsonReader(FileReader("src/main/resources/examsAnswered2.json"))
        val madeGeneratedExams: MutableList<AnsweredExam> = gson.fromJson<Array<AnsweredExam>>(
                reader1, Array<AnsweredExam>::class.java).toMutableList()

        val answeredExam = AnsweredExam(
                examId = i,
                examDate = Date(),
                answeredQuestions = practiceExam.questions.map {
                    AnsweredQuestion(
                            questionId = it.questionId!!,
                            resultWasGood = ThreadLocalRandom.current().nextBoolean(),
                            questionText = it.questionText!!,
                            categories = it.categories.toTypedArray(),
                            answeredOn = Date(),
                            questionType = it.questionType,
                            answerType = it.answerType)
                }.toTypedArray()
        )

        madeGeneratedExams.add(answeredExam)
        File("src/main/resources/examsAnswered2.json").writeText(gson.toJson(madeGeneratedExams), Charsets.UTF_8)
    }

}

class GeneratePersonalPracticeExam(val QuestionDAO: QuestionDAO) {

    fun generatePracticeExam(studentNumber: Int, courseId: Int): PracticeExam {

        val allQuestionsFromCourse: Array<Question> = try {
            QuestionDAO.getAllQuestionsFromCourse(courseId)
        } catch (e: DatabaseException) {
            // Add Logger
            throw e
        }

        val answeredQuestionsByRelevantOthers: Map<Int, Map<Int, AnsweredQuestion>> = try {
            QuestionDAO.getAnsweredQuestionsByRelevantOthers(studentNumber)
        } catch (e: DatabaseException) {
            // Add Logger
            throw e
        }

        val answeredExams: Array<AnsweredExam> = try {
            QuestionDAO.getAllAnsweredExamsByStudent(studentNumber)
        } catch (e: DatabaseException) {
            // Add Logger
            throw e
        }

        val answeredQuestions = answeredExams
                .map { it.answeredQuestions }.toTypedArray()
                .filter { it != null }.toTypedArray()
                .flatten().toTypedArray()

        // ExamID, Relevance
        val examRelevance = calculateWeightForExams(
                answeredExams.map { Pair(it.examId, it.answeredQuestions) }.toMap())

        val questionRelevance = allQuestionsFromCourse
                .map {
                    Pair(
                            it.questionId!!,
                            arrayOf(
                                    contentBasedFiltering(allQuestionsFromCourse, answeredQuestions, it.questionId),
                                    collaborativeFiltering(answeredQuestions, answeredQuestionsByRelevantOthers, it.questionId),
                                    examRelevance.getOrDefault(it.questionId, 0.0)
                            ).average())
                }
                .toList()
                .sortedBy { (_, value) -> value }
                .asReversed()
                .toMap()

        println(questionRelevance.map { it.key })

        // --- GENERATING PRACTICE EXAM --- //

        val questionsMap: Map<Int, Question> = allQuestionsFromCourse.map { Pair(it.questionId!!, it) }.toMap()
        val examQuestions: ArrayList<Question> = ArrayList()
        val categoriesInExam: MutableMap<String, Int> = HashMap()
        var totalCategoriesInExam = 0

            for (questionID: Int in questionRelevance.keys) {
                val question: Question = questionsMap[questionID]!!

                // For each question calculate the percentage of the categories in the question that are already in the exam
                for (category in question.categories) {
                    // If there are no categories added to the exam (No questions)
                    // Than add the most relevant one.
                    var questionAdded = false
                    if (totalCategoriesInExam == 0) {
                        question.questionOrderInExam = examQuestions.size + 1
                        examQuestions.add(question)
                        questionAdded = true
                    } else {
                        // Percentage of the current category in the exam
                        val categoryPercentage = 100 / totalCategoriesInExam * categoriesInExam.getOrDefault(category, 0)

                        // Category should not have more than 50% of one exam, so random got til 50
                        val random = ThreadLocalRandom.current().nextInt(50)
                        // If the random number is higher than the current percentage of the category in the exam.
                        // Than add the question to the exam
                        if (random > categoryPercentage) {
                            question.questionOrderInExam = examQuestions.size + 1
                            examQuestions.add(question)
                            questionAdded = true
                        }
                    }

                    if (questionAdded) {
                        // Update the category map with the categories of the added exam
                        for (category2 in question.categories) {
                            categoriesInExam[category2] = (categoriesInExam.getOrDefault(category2, 0)) + 1
                            totalCategoriesInExam++
                        }
                        // Break for loop because a question was added
                        break
                    }
                }

                // If the exam does have 10 or more questions quit adding new questions
                if (examQuestions.size >= 10){
                    break
                }
            }


        println(examQuestions.map { it.questionId })

        return PracticeExam("Practice Exam (${LocalDateTime.now()})", courseId, examQuestions.toTypedArray())
    }
}

internal fun Boolean.toInteger() = if (this) -1 else 1
