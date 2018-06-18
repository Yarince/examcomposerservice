package nl.han.ica.examplatform.persistence.exam.results

import nl.han.ica.examplatform.business.exam.practice.Results
import nl.han.ica.examplatform.business.exam.practice.models.QuestionResult
import nl.han.ica.examplatform.business.exam.practice.models.QuestionResultStats

interface IExamResultsDAO {
    fun getResultsOfOthersInCategory(studentId: Int, category: String): ArrayList<QuestionResultStats>

    fun getQuestionsAnsweredByStudentInCourse(studentId: Int, courseId: Int): ArrayList<QuestionResult>

    fun getPreviousResultsOfStudent(studentId: Int, courseId: Int): ArrayList<Results>
}