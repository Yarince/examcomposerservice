package nl.han.ica.examplatform.persistence.exam

import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@Repository
class ExamDAOStub {

    fun insertExam(exam: Exam): Exam {
        println(exam)
        // Database logic needs to be added here

        val insertedExam = exam.copy()
        return insertedExam
    }

    fun updateExam(exam: Exam): Exam {
        println(exam)
        // Database logic needs to be added here

        val updatedExam = exam.copy()
        return updatedExam
    }
}