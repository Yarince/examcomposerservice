package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.models.exam.PracticeExam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import java.util.concurrent.ThreadLocalRandom

fun generateExam(courseId: Int, categories: Array<String>): PracticeExam {
    val questions = QuestionDAO().getQuestions(courseId, categories)

    // Group questions by tag
    val possibleSubjects = questions.groupBy { it.categories }.toMutableMap()
    // Put all subject keys in a list
    val possibleSubjectsKeysArray = questions.groupBy { it.categories }.keys.toList()

    // The list of which the questions should be added to
    val practiceExam = ArrayList<Question>()

    // Recursively add questions to exam
    addQuestionsToExam(questions, practiceExam, possibleSubjects, possibleSubjectsKeysArray)

    return PracticeExam(name = "Practice exam", courseId = courseId, questions = practiceExam)
}

fun addQuestionsToExam(questions: Array<Question>, exam: ArrayList<Question>, possibleSubjects: MutableMap<Array<String>, List<Question>>, possibleSubjectsKeysArray: List<Array<String>>, iterator: Int = 0, iteratorForward: Boolean = true) {
    // If the exam contains 50% of the questions, exit this function
    if (exam.size > 0) if (exam.size % (questions.size / 1) == 0) return

    // Gets the list of questions in the current subject
    val currentSubjectList = possibleSubjects[possibleSubjectsKeysArray[iterator]]

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