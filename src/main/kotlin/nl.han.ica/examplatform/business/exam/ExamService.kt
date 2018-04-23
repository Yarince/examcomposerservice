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

    fun validateEmptyId(exam: Exam) {
        // Check if the examId is empty
        if (exam.examId != null)
            throw InvalidExamException("examId must be left empty")
    }

    fun validateEmptyQuestions(exam: Exam) {
        if (exam.questions !=null)
            throw InvalidExamException("questions must be empty")
    }

    fun getExams(): Array<Exam> {
        // Fetch from DB
        return examDAO.getAllExams()
    }

    fun addExam(exam: Exam): ResponseEntity<Exam> {
        // Check if exam has a correct syntax
        validateEmptyId(exam)
        validateEmptyQuestions(exam)

        val insertedObject = examDAO.insertExam(exam) //Add to database

        return ResponseEntity(insertedObject, HttpStatus.CREATED)
    }
}
