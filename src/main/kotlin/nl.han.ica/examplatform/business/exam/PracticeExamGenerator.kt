package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.models.exam.PracticeExam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import java.util.concurrent.ThreadLocalRandom

/**
 * Generates a practice exam based on course and categories
 *
 * @param courseId [Int] the ID of the course for which the exam should be generated
 * @param categories [Array]<[String]> the subjects of which the exam should be about
 * @param questionDAO the DAO that is needed to retrieve the questions, because it can't be injected
 *
 * @return [PracticeExam] returns a practiceExam
 */
fun generatePracticeExam(courseId: Int, categories: Array<String>, questionDAO: QuestionDAO): PracticeExam {
    val questions = questionDAO.getQuestionsByCourseAndCategory(courseId, categories)

    // Recursively add questions to exam
    val practiceExam = addQuestionsToPracticeExam(questions, questions, categories.toList())

    return PracticeExam(name = "Practice exam", courseId = courseId, questions = practiceExam)
}

/**
 * Recursive function that iterates over the available categories and adds questions to it
 *
 * @param questions [Array]<[Question]> all the available questions
 * @param strippedQuestions [Array]<[String]> the subjects of which the exam should be about
 * @param subjectsAvailable [List]<[String]> the available subjects
 * @param iterator [Int] the iterator is used to iterate through the available subjects
 * @param iteratorForward [Boolean] this boolean indicates which direction the iterator is going
 * @param exam [ArrayList]<[Question]> the practiceExam to which the questions are being added
 *
 * @return [ArrayList]<[Question]> An array of questions, representing a practiceExam
 */
private fun addQuestionsToPracticeExam(questions: Array<Question>, strippedQuestions: Array<Question>, subjectsAvailable: List<String>, iterator: Int = 0, iteratorForward: Boolean = true, exam: ArrayList<Question> = arrayListOf()): ArrayList<Question> {
    // If the exam contains 50% of the questions, exit this function
    if (exam.size > 0) if (exam.size >= (questions.size / 2)) return exam
    if (subjectsAvailable.isEmpty()) return exam

    // Gets the list of questions in the current subject
    val currentSubjectList: List<Question>?

    subjectsAvailable.elementAtOrElse(iterator, { return exam }) // todo: create exception for this
    // get questions grouped in current category

    currentSubjectList = strippedQuestions.filter { it.categories.contains(subjectsAvailable[iterator]) }

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
    if (newIt >= subjectsAvailable.size - 1)
        newItForward = false
    else if (newIt < 1)
        newItForward = true

    // Recursively add more questions
    if (questionToAdd == null) {
        addQuestionsToPracticeExam(questions, strippedQuestions, subjectsAvailable, newIt, newItForward, exam)
    } else {
        val newQuestionsList = strippedQuestions.toMutableList()
        newQuestionsList.removeAll { it.questionId == questionToAdd!!.questionId }

        addQuestionsToPracticeExam(questions, newQuestionsList.toTypedArray(), subjectsAvailable, newIt, newItForward, exam)
    }
    return exam
}