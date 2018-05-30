package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.responseexceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.PracticeExam
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.persistence.exam.ExamDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

/**
 * Exam service for handling requests related to the Exam model.
 */
@Service
class ExamService {
    private val logger = loggerFor(javaClass)

    @Autowired
    lateinit var examDAO: ExamDAO

    /**
     * Check if an exam has questions and if the id is left empty
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
     * Get all Exams from the database
     *
     * @return [ResponseEntity]<Array<[SimpleExam]>> All exams currently in the database in a simplified view
     */
    fun getExams(): ResponseEntity<ArrayList<SimpleExam>> {
        return ResponseEntity(examDAO.getExams(), HttpStatus.OK)
    }

    /**
     * Add an new Exam to the database
     *
     * @param exam [Exam] to be added in the database
     * @return ResponseEntity<[Exam]> with new exam inserted and an assigned id
     */
    fun addExam(exam: Exam): ResponseEntity<Exam> {
        checkExam(exam)
        val insertedObject = examDAO.insertExam(exam) //Add to database
        return ResponseEntity(insertedObject, HttpStatus.CREATED)
    }

    /**
     * Get a specific Exam from the database
     *
     * @return [ResponseEntity]<[Exam]> Fetched from the database
     */
    fun getExam(id: Int): ResponseEntity<Exam> {
        return ResponseEntity(examDAO.getExam(id), HttpStatus.OK)
    }

    /**
     * Generate a practice practice [Exam]
     *
     * @return [ResponseEntity]<Exam> practice [Exam]
     */
    fun generatePracticeExam(courseId: Int): ResponseEntity<PracticeExam?> {
        val practiceExam: PracticeExam? = examDAO.generatePracticeExam(courseId)
        return ResponseEntity(practiceExam, HttpStatus.CREATED)
    }
}

