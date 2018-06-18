package nl.han.ica.examplatform.persistence.exam.results

import nl.han.ica.examplatform.business.exam.practice.Results
import nl.han.ica.examplatform.business.exam.practice.models.QuestionResult
import nl.han.ica.examplatform.business.exam.practice.models.QuestionResultStats
import org.springframework.stereotype.Repository

@Repository
class ExamResultsDAO: IExamResultsDAO {
    override fun getPreviousResultsOfStudent(studentId: Int, courseId: Int): ArrayList<Results> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResultsOfOthersInCourse(courseId: Int): ArrayList<QuestionResultStats> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getQuestionsAnsweredByStudentInCourse(studentId: Int, courseId: Int): ArrayList<QuestionResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}