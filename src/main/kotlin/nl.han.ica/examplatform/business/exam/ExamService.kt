package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.persistence.exam.ExamDAOStub
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


@Service
class ExamService {

    @Autowired
    lateinit var examDAOStub: ExamDAOStub

    fun getExams(): ResponseEntity<Array<SimpleExam>> {
        return ResponseEntity(examDAOStub.getExams(), HttpStatus.OK)
    }

    fun addExam(exam: Exam): ResponseEntity<Exam> {
        ExamValidator.validate(exam) // Check if exam has a correct syntax
        examDAOStub.insertExam(exam) //Add to database
        return ResponseEntity(exam, HttpStatus.CREATED)
    }

    fun getExam(id: Int): ResponseEntity<Exam> {
        return ResponseEntity(examDAOStub.getExam(id), HttpStatus.OK)
    }
}
