package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.controllers.responseexceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.persistence.exam.ExamDAO
import nl.han.ica.examplatform.persistence.exam.ExamDAOStub
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


@Service
class ExamService {

    @Autowired
    lateinit var examDAOStub: ExamDAOStub

    @Autowired
    lateinit var examDAO: ExamDAO

    /**
     * Check if an exam has questions and if the id is left empty
     */
    fun checkExam(exam: Exam) {
        if (exam.questions != null) throw InvalidExamException("questions must be empty")
        if (exam.examId != null) throw InvalidExamException("examId must be left empty")
    }

    fun getExams(): ResponseEntity<ArrayList<SimpleExam>> {
        return ResponseEntity(examDAO.getExams(), HttpStatus.OK)
    }

    fun addExam(exam: Exam): ResponseEntity<Exam> {
        checkExam(exam)
        val insertedObject = examDAOStub.insertExam(exam) //Add to database
        return ResponseEntity(insertedObject, HttpStatus.CREATED)
    }

    fun getExam(id: Int): ResponseEntity<Exam> {
        return ResponseEntity(examDAOStub.getExam(id), HttpStatus.OK)
    }
}

