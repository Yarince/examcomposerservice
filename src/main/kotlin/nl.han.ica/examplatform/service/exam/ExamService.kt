package nl.han.ica.examplatform.service.exam

import nl.han.ica.examplatform.business.exam.validator.ExamValidator
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.persistence.exam.DaoStub
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ExamService {
    fun getExams(): Array<Exam> {
        return Array(2, { i -> Exam(i, "name-$i") })
        // Example returns a array of 2 exams
    }

    fun addExam(exam: Exam): ResponseEntity<Exam> {
        ExamValidator.validate(exam) // Check if exam has a correct syntax
        DaoStub(exam) //Add to database
        return ResponseEntity(exam, HttpStatus.CREATED)
    }
}
