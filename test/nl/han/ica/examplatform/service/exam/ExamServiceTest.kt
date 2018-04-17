package nl.han.ica.examplatform.service.exam

import nl.han.ica.examplatform.models.exam.Exam
import org.junit.Assert.*

import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Test


@RunWith(MockitoJUnitRunner::class)
internal class ExamServiceTest {

    @InjectMocks
    lateinit var examService: ExamService

    @Test
    fun getExams() {
        val result = examService.getExams()
        assertNotNull(result)
        assertArrayEquals(arrayOf(
                Exam(0, "name-0"),
                Exam(1, "name-1")),
                result)
    }

    @Test
    fun addExam() {
        val expected = Exam(0, "name-0")
        val result = examService.addExam(expected)
        assertNotNull(result)
        assertEquals(expected, result)
    }
}