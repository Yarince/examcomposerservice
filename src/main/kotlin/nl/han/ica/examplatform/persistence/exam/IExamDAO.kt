package nl.han.ica.examplatform.persistence.exam

import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.PreparedExam
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.models.question.Question
import org.springframework.http.HttpStatus

/**
 * This class handles all the Database operations for [Exam].
 */
interface IExamDAO {

    /**
     * This function gets a list of minimized Exams.
     *
     * @return [ArrayList]<[SimpleExam]> a list of SimpleExams retrieved from the database.
     */
    fun getExams(): ArrayList<SimpleExam>

    /**
     * Gets all information about an Exam.
     *
     * @param id [Int] The ID of which all information should be queried
     * @return [Exam] The Exam added to the database.
     */
    fun getExam(id: Int): Exam

    /**
     * Inserts an Exam into the database.
     *
     * @param exam [Exam] The Exam that should be inserted
     * @return [Exam] The Exam added to the database
     */
    fun insertExam(exam: Exam): Exam

    /**
     * Adds single or multiple questions to an exam.
     *
     * @param exam [Exam] The containing all [Question]s
     * @return [Exam] The updated Exam that was given.
     */
    fun addQuestionsToExam(exam: Exam): Exam

    /**
     * Adds a class to an exam.
     *
     * @param examId [Int] the ID of the exam
     * @param classes [Array]<[String]> an array containing classes
     * @return [PreparedExam] the exam containing the added classes
     */
    fun addClassesToExam(examId: Int, classes: ArrayList<String>) : HttpStatus

    /**
     * Changes the order of questions in an exam
     *
     * @param examId [Int] The ID of the exam
     * @param questionsAndSequenceNumbers [Array]<[Pair]<[Int], [Int]>> An array containing the questionIds and the new sequence number
     */
    fun changeQuestionOrderInExam(examId: Int, questionsAndSequenceNumbers: Array<Pair<Int, Int>>)

    /**
     * Publishes an exam.
     *
     * @param examId [Int] The ID of the exam
     * @param shouldBePublished [Boolean] Indicates whether the exam should be published or un-published
     */
    fun publishExam(examId: Int, shouldBePublished: Boolean)

    /**
     * Updates the meta data of an exam.
     *
     * @param exam [Exam] The Exam to update
     * @return [Exam] The updated exam
     */
    fun updateExam(exam: Exam): Exam

    /**
     * De-couples questions from an exam.
     *
     * @param examId [Int] The ID of the exam
     * @param questionIds [Array]<[Int]> Array containing the IDs of the questions that should be removed
     */
    fun removeQuestionsFromExam(examId: Int, questionIds: Array<Int>)

    /**
     * Deletes an exam.
     * This doesn't delete any questions.
     *
     * @param examId [Int] The ID of the exam that should be deleted
     */
    fun deleteExam(examId: Int)
}