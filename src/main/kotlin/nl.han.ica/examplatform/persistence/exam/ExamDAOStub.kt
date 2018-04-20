package nl.han.ica.examplatform.persistence.exam

import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@Repository
class ExamDAOStub {

    val counter = AtomicInteger()

    fun insertExam(exam: Exam): Exam {
        println(exam)
        // Database logic needs to be added here
        return exam
    }

    fun getAllExams(): Array<Exam> {
        // Database logic needs to be added here
        // Example returns a array of 2 exams
        return Array(2, { i -> Exam(name = "name-$i", durationInMinutes = 10, startTime = Date(6000), course = "APP", version = 1, examType = ExamType.EXAM) })
    }

    fun updateExam(exam: Exam): Exam {
        println(exam)
        // Database logic needs to be added here
        return exam
    }
}