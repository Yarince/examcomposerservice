package nl.han.ica.examplatform.persistence.exam.results

import nl.han.ica.examplatform.business.exam.practice.models.QuestionResult
import nl.han.ica.examplatform.business.exam.practice.models.QuestionResultStats

interface IExamResultsDAO {
    fun getResultsOfOthersInCourse(courseId: Int): ArrayList<QuestionResultStats>

    fun getQuestionsAnsweredByStudentInCourse(studentId: Int, courseId: Int): ArrayList<QuestionResult>

}