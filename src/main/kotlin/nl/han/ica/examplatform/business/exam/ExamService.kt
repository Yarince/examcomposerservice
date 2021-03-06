package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.business.exam.practice.generatePracticeExam
import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.exam.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.PracticeExam
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.persistence.category.ICategoryDAO
import nl.han.ica.examplatform.persistence.exam.ExamDAO
import nl.han.ica.examplatform.persistence.exam.IExamDAO
import nl.han.ica.examplatform.persistence.exam.results.IExamResultsDAO
import nl.han.ica.examplatform.persistence.question.IQuestionDAO
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

/**
 * Exam service for handling requests related to the [Exam] model.
 *
 * @param examDAO [IExamDAO] The ExamDAO
 * @param questionDAO [IQuestionDAO] The QuestionDAO
 * @param categoryDAO [ICategoryDAO] the CategoryDAO
 * @param examResultsDAO [IExamResultsDAO] the ExamResultDAO
 */
@Service
class ExamService(private val examDAO: IExamDAO,
                  private val questionDAO: IQuestionDAO,
                  private val categoryDAO: ICategoryDAO,
                  private val examResultsDAO: IExamResultsDAO) {


    private val logger = loggerFor(javaClass)

    /**
     * Check if an exam has questions and if the id is left empty.
     *
     * @param exam [Exam] that needs to be validated
     * @throws InvalidExamException If properties of the exam are not correct
     */
    fun checkExam(exam: Exam?) {
        if (exam?.questions != null) {
            logger.error("Tried to insert an exam with questions when questions should've been")
            throw InvalidExamException("questions must be empty")
        }
        if (exam?.examId != null) {
            logger.error("Tried to insert an exam with an examId predefined")
            throw InvalidExamException("examId must be left empty")
        }
    }

    /**
     * Get all Exams from the database.
     *
     * @return [ResponseEntity]<Array<[SimpleExam]>> All exams currently in the database in a simplified view
     */
    fun getExams(): ResponseEntity<ArrayList<SimpleExam>> =
            ResponseEntity(examDAO.getExams(), HttpStatus.OK)

    /**
     * Add an new Exam to the database.
     *
     * @param exam [Exam] to be added in the database
     * @return [ResponseEntity]<[Exam]> with new exam inserted and an assigned id
     */
    fun addExam(exam: Exam): ResponseEntity<Exam> {
        checkExam(exam)
        // Insert Exam into database
        val insertedObject = examDAO.insertExam(exam)
        return ResponseEntity(insertedObject, HttpStatus.CREATED)
    }

    /**
     * Get a specific Exam from the database.
     *
     * @param id [Int] The ID of the exam that should be retrieved
     * @return [ResponseEntity]<[Exam]> Fetched from the database
     */
    fun getExam(id: Int): ResponseEntity<Exam> =
            ResponseEntity(examDAO.getExam(id).copy(questions = questionDAO.getQuestionsByExam(id)), HttpStatus.OK)

    /**
     * Generates a [PracticeExam].
     *
     * @param courseId [Int] The ID of the course of which the exam should be generated
     * @return [ResponseEntity]<Exam> practice [PracticeExam]
     */
    fun generatePersonalPracticeExam(courseId: Int, studentNr: Int): ResponseEntity<PracticeExam> =
            ResponseEntity(generatePracticeExam(courseId, studentNr, questionDAO, categoryDAO, examResultsDAO), HttpStatus.CREATED)

    /**
     * Adds a class to an exam.
     *
     * @param examId [Int] the ID of the exam
     * @param classes [Array]<[String]> an array containing classes
     * @return [ResponseEntity]<[HttpStatus] The response of the retrieved classes
     */
    fun addClassesToExam(examId: Int, classes: ArrayList<String>): ResponseEntity<HttpStatus> =
            ResponseEntity(examDAO.addClassesToExam(examId, classes), HttpStatus.ACCEPTED)

    /**
     * Updates the meta data of an exam.
     *
     * @param exam [Exam] The Exam to update
     * @return [Exam] The updated exam
     */
    fun updateExam(exam: Exam) = ResponseEntity(examDAO.updateExam(exam), HttpStatus.ACCEPTED)

    /**
     * Publishes an exam.
     *
     * @param examId [Int] The ID of the exam that should be published
     * @param shouldBePublished [Boolean] Indicates whether the exam should be published or un-published. Default true
     */
    fun publishExam(examId: Int, shouldBePublished: Boolean = true) = examDAO.publishExam(examId, shouldBePublished)

    /**
     * Deletes an exam.
     * This doesn't delete any questions.
     *
     * @param examId [Int] The ID of the exam that should be deleted
     */
    fun deleteExam(examId: Int) = examDAO.deleteExam(examId)
}
