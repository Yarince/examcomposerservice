package nl.han.ica.examplatform.persistence.exam

import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ExamDAOStub {
    fun insertExam(exam: Exam): Exam {
        return exam
    }
    fun getExam(id: Int): Exam {
        return Exam(examId = id, durationInMinutes = 10, startTime = Date(6000), course = "APP", examType = ExamType.EXAM, name = "Exam 1")
    }

}