package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.controllers.responseexceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.OfficialExam
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
class OfficialExamService {

    @Autowired
    lateinit var examDAO: ExamDAO

    /**
     * Check if an officialExam has questions and if the id is left empty
     *
     * @param officialExam [OfficialExam] that needs to be validated
     * @throws InvalidExamException If properties of the exam are not correct
     */
    fun checkExam(officialExam: OfficialExam) {
        if (officialExam.questions != null) throw InvalidExamException("questions must be empty")
        if (officialExam.examId != null) throw InvalidExamException("examId must be left empty")
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
     * @param officialExam [OfficialExam] to be added in the database
     * @return ResponseEntity<[OfficialExam]> with new exam inserted and an assigned id
     */
    fun addExam(officialExam: OfficialExam): ResponseEntity<OfficialExam> {
        checkExam(officialExam)
        val insertedObject = examDAO.insertExam(officialExam) //Add to database
        return ResponseEntity(insertedObject, HttpStatus.CREATED)
    }

    /**
     * Get a specific Exam from the database
     *
     * @return [ResponseEntity]<[OfficialExam]> Fetched from the database
     */
    fun getExam(id: Int): ResponseEntity<OfficialExam> {
        return ResponseEntity(examDAO.getExam(id), HttpStatus.OK)
    }
}

