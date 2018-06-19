package nl.han.ica.examplatform.persistence.exam.results

import nl.han.ica.examplatform.models.exam.PracticeExamResult
import nl.han.ica.examplatform.models.question.QuestionResultStats

/**
 * Interface for Exam PracticeExamResult DAO
 */
interface IExamResultsDAO {
    /**
     * Gets the combined results of other students in a specific category.
     *
     * @param studentNr [Int] the student nr.
     * @param category [String] the category
     * @return [ArrayList]<[QuestionResultStats]>
     */
    fun getResultsOfOthersInCategory(studentNr: Int, category: String): ArrayList<QuestionResultStats>

    /**
     * Gets the previous results of a student in a course.
     *
     * @param studentNr [Int] the student nr.
     * @param courseId [Int] the courseID
     * @return [ArrayList]<[PracticeExamResult]>
     */
    fun getPreviousResultsOfStudent(studentNr: Int, courseId: Int): ArrayList<PracticeExamResult>
}
