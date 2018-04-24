package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.controllers.responseExceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import org.junit.Test
import java.util.*

internal class ExamValidatorTest {
    @Test(expected = InvalidExamException::class)
    fun testValidate() {
        val exam = Exam("name-0", 10, Date(6000), "APP", ExamType.EXAM,
                5) // Faulty exam object

        ExamValidator.validate(exam)
    }
}