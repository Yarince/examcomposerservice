package nl.han.ica.examplatform.persistence.exam.results

import nl.han.ica.examplatform.business.exam.practice.Results
import nl.han.ica.examplatform.business.exam.practice.models.QuestionResultStats

interface IExamResultsDAO {
    fun getResultsOfOthersInCategory(studentNr: Int, category: String): ArrayList<QuestionResultStats>

    fun getPreviousResultsOfStudent(studentNr: Int, courseId: Int): ArrayList<Results>
}