package nl.han.ica.examplatform.persistence.exam.results

import nl.han.ica.examplatform.business.exam.practice.models.QuestionResult
import nl.han.ica.examplatform.business.exam.practice.models.QuestionResultStats

class ExamResultsDAO: IExamResultsDAO {
    override fun getResultsOfOthersInCourse(courseId: Int): ArrayList<QuestionResultStats> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getQuestionsAnsweredByStudentInCourse(studentId: Int, courseId: Int): ArrayList<QuestionResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}