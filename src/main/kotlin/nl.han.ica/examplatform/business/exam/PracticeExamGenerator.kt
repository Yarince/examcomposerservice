package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.models.exam.PracticeExam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import org.springframework.beans.factory.annotation.Autowired
import java.lang.IndexOutOfBoundsException
import java.util.concurrent.ThreadLocalRandom

fun generateExam(courseId: Int, categories: Array<String>, questionDAO: QuestionDAO): PracticeExam {
    val questions = questionDAO.getQuestions(courseId, categories)

    val possibleSubjects: MutableMap<String, List<Question>> = mutableMapOf()
    for (category in categories) {
        possibleSubjects[category] = questions.filter { it.categories.contains(category) }
    }

    // The list of which the questions should be added to
    val practiceExam = ArrayList<Question>()

    // Recursively add questions to exam
    addQuestionsToExam(questions, practiceExam, possibleSubjects, categories.toList())

    return PracticeExam(name = "Practice exam", courseId = courseId, questions = practiceExam)
}

private fun addQuestionsToExam(questions: Array<Question>, exam: ArrayList<Question>, possibleSubjects: MutableMap<String, List<Question>>, possibleSubjectsKeysArray: List<String>, iterator: Int = 0, iteratorForward: Boolean = true) {
    // If the exam contains 50% of the questions, exit this function
    if (exam.size > 0) if (exam.size % (questions.size / 2) == 0) return

    // Gets the list of questions in the current subject
    val currentSubjectList: List<Question>?
    try {
        possibleSubjectsKeysArray.elementAtOrElse(iterator, { println("throw") }) // todo: create exception for this
        if (!possibleSubjects.containsKey(possibleSubjectsKeysArray[iterator])) println("2nd throw")
        currentSubjectList = possibleSubjects[possibleSubjectsKeysArray[iterator]]
    } catch (e: IndexOutOfBoundsException) {
        println(e) // todo better error handling
        return
    }

    // If it's not null, add a random question to the exam
    currentSubjectList?.let {
        if (it.isEmpty()) return@let
        val randomNumber = ThreadLocalRandom.current().nextInt(0, it.size)
        exam.add(it[randomNumber])

        // Remove the question that was just added, so no duplicates are in the exam
        val mutableQuestionList = it.toMutableList()
        mutableQuestionList.remove(it[randomNumber])
        possibleSubjects.replace(possibleSubjectsKeysArray[iterator], mutableQuestionList.toList())
    }

    // This makes it so the questions are cycled between subjects
    val newIt = if (iteratorForward) iterator + 1 else iterator - 1

    var newItForward: Boolean = iteratorForward
    if (newIt >= possibleSubjectsKeysArray.size - 1)
        newItForward = false
    else if (newIt < 1)
        newItForward = true

    // Recursively add more questions
    addQuestionsToExam(questions, exam, possibleSubjects, possibleSubjectsKeysArray, newIt, newItForward)
}