package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.controllers.responseexceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.OfficialExam
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.persistence.exam.ExamDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


@Service
class OfficialExamService {

    @Autowired
    lateinit var examDAO: ExamDAO

    /**
     * Check if an officialExam has questions and if the id is left empty
     */
    fun checkExam(officialExam: OfficialExam) {
        if (officialExam.questions != null) throw InvalidExamException("questions must be empty")
        if (officialExam.examId != null) throw InvalidExamException("examId must be left empty")
    }

    fun getExams(): ResponseEntity<ArrayList<SimpleExam>> {
        return ResponseEntity(examDAO.getExams(), HttpStatus.OK)
    }

    fun addExam(officialExam: OfficialExam): ResponseEntity<OfficialExam> {
        checkExam(officialExam)
        val insertedObject = examDAO.insertExam(officialExam) //Add to database
        return ResponseEntity(insertedObject, HttpStatus.CREATED)
    }

    fun getExam(id: Int): ResponseEntity<OfficialExam> {
        return ResponseEntity(examDAO.getExam(id), HttpStatus.OK)
    }
}

