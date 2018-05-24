package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.models.exam.PracticeExam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import java.util.concurrent.ThreadLocalRandom

fun generateExam(courseId: Int, categories: Array<String>, questionDAO: QuestionDAO): PracticeExam {
    val questions = questionDAO.getQuestions(courseId, categories)

    // Recursively add questions to exam
    val practiceExam = addQuestionsToExam(questions, questions, categories.toList())

    return PracticeExam(name = "Practice exam", courseId = courseId, questions = practiceExam)
}

private fun addQuestionsToExam(questions: Array<Question>, questionsPerCategory: Array<Question>, questionsAvailable: List<String>, iterator: Int = 0, iteratorForward: Boolean = true, exam: ArrayList<Question> = arrayListOf()): ArrayList<Question> {
    // If the exam contains 50% of the questions, exit this function
    if (exam.size > 0) if (exam.size >= (questions.size / 2)) return exam
    if (questionsAvailable.isEmpty()) return exam

    // Gets the list of questions in the current subject
    val currentSubjectList: List<Question>?

    questionsAvailable.elementAtOrElse(iterator, { return exam }) // todo: create exception for this
    // get questions grouped in current category

    currentSubjectList = questionsPerCategory.filter { it.categories.contains(questionsAvailable[iterator]) }

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
    if (newIt >= questionsAvailable.size - 1)
        newItForward = false
    else if (newIt < 1)
        newItForward = true

    // Recursively add more questions
    if (questionToAdd == null) {
        addQuestionsToExam(questions, questionsPerCategory, questionsAvailable, newIt, newItForward, exam)
    } else {
        val newQuestionsList = questionsPerCategory.toMutableList()
        newQuestionsList.removeAll { it.questionId == questionToAdd!!.questionId }

        addQuestionsToExam(questions, newQuestionsList.toTypedArray(), questionsAvailable, newIt, newItForward, exam)
    }
    return exam
}