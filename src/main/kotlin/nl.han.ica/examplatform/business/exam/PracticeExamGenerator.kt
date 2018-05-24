package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.models.exam.PracticeExam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import java.lang.IndexOutOfBoundsException
import java.util.concurrent.ThreadLocalRandom

fun generateExam(courseId: Int, categories: Array<String>, questionDAO: QuestionDAO): PracticeExam {
    val questions = questionDAO.getQuestions(courseId, categories)

    // The list of which the questions should be added to
    val practiceExam = ArrayList<Question>()

    // Recursively add questions to exam
    addQuestionsToExam(questions, practiceExam, questions, categories.toList())

    return PracticeExam(name = "Practice exam", courseId = courseId, questions = practiceExam)
}

@Synchronized
private fun addQuestionsToExam(questions: Array<Question>, exam: ArrayList<Question>, questionsPerCategory: Array<Question>, categoriesAvailable: List<String>, iterator: Int = 0, iteratorForward: Boolean = true) {
    // If the exam contains 50% of the questions, exit this function
    if (exam.size > 0) if (exam.size % (questions.size / 2) == 0) return

    // Gets the list of questions in the current subject
    val currentSubjectList: List<Question>?
    try {
        categoriesAvailable.elementAtOrElse(iterator, { return }) // todo: create exception for this
        // get questions grouped in current category

        currentSubjectList = questionsPerCategory.filter { it.categories.contains(categoriesAvailable[iterator]) }
    } catch (e: IndexOutOfBoundsException) {
        println(e) // todo better error handling
        return
    }

    var questionToAdd: Question? = null

    // If it's not null, add a random question to the exam
    currentSubjectList.let {
        if (it.isEmpty()) return@let
        val randomNumber = ThreadLocalRandom.current().nextInt(0, it.size)
        questionToAdd = it[randomNumber]
        questionToAdd?.let {
            exam.add(it)
        }
    }

    // This makes it so the questions are cycled between subjects
    val newIt = if (iteratorForward) iterator + 1 else iterator - 1
    var newItForward: Boolean = iteratorForward
    if (newIt >= categoriesAvailable.size - 1)
        newItForward = false
    else if (newIt < 1)
        newItForward = true

    // Recursively add more questions
    if (questionToAdd == null) {
        addQuestionsToExam(questions, exam, questionsPerCategory, categoriesAvailable, newIt, newItForward)
    } else {
        val newQuestionsList = questionsPerCategory.toMutableList()
        newQuestionsList.removeAll { it.questionId == questionToAdd!!.questionId }

        addQuestionsToExam(questions, exam, newQuestionsList.toTypedArray(), categoriesAvailable, newIt, newItForward)
    }
}