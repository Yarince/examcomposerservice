package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.controllers.responseexceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.persistence.exam.ExamDAOStub
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ExamService {

    @Autowired
    lateinit var examDAO: ExamDAOStub

    /**
     * Check if an exam has questions and if the id is left empty
     */
    fun checkExam(exam: Exam) {
        if (exam.questions != null) throw InvalidExamException("questions must be empty")
        if (exam.examId != null) throw InvalidExamException("examId must be left empty")
    }

    fun addExam(exam: Exam): ResponseEntity<Exam> {
        checkExam(exam)

        val insertedObject = examDAO.insertExam(exam) //Add to database

        return ResponseEntity(insertedObject, HttpStatus.CREATED)
    }
}
