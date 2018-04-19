package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.business.exam.ExamService
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import org.junit.Assert.*

import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*


@RunWith(MockitoJUnitRunner::class)
internal class ExamServiceTest {

    @InjectMocks
    lateinit var examService: ExamService

    @Test
    fun getExams() {
        val expected = arrayOf(
                Exam("name-0", 10, Date(6000), "APP", ExamType.EXAM),
                Exam("name-1", 10, Date(6000), "APP", ExamType.EXAM))

        val result = examService.getExams()
        assertNotNull(result)
        assertArrayEquals(expected, result)
    }

    @Test
    fun addExam() {
        val expected = Exam("name-0", 10, Date(6000), "APP", ExamType.EXAM)
        val result = examService.addExam(expected)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected,HttpStatus.CREATED), result)
    }
}