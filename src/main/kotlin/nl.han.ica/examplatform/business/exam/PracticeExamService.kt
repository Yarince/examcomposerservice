package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.controllers.responseexceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.PracticeExam
import nl.han.ica.examplatform.persistence.exam.ExamDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PracticeExamService {

    @Autowired
    private
    lateinit var examDAO: ExamDAO

    fun checkExam(exam: Exam?) {
        if (exam?.questions != null) throw InvalidExamException("questions must be empty")
        if (exam?.examId != null) throw InvalidExamException("examId must be left empty")
    }

    fun generatePracticeExam() : ResponseEntity<PracticeExam?> {
        val practiceExam: PracticeExam? = examDAO.generatePracticeExam()
        checkExam(practiceExam)
        return ResponseEntity(practiceExam, HttpStatus.CREATED)
    }
}