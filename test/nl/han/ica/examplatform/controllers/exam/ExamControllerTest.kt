package nl.han.ica.examplatform.controllers.exam

import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.service.exam.ExamService
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals


@RunWith(MockitoJUnitRunner::class)
class ExamControllerTest {

    @InjectMocks
    private
    lateinit var examController: ExamController

    @Mock
    private
    lateinit var examService: ExamService

    @Test
    fun testGetExams() {
        doReturn(arrayOf(
                Exam(0, "name-0"),
                Exam(1, "name-1"))
        ).`when`(examService).getExams()

        val result = examController.getExams()
        assertNotNull(result)
        assertArrayEquals(arrayOf(
                Exam(0, "name-0"),
                Exam(1, "name-1")),
                result)
    }

    @Test
    fun testAddExam() {
        val expected = Exam(0, "test name")
        doReturn(ResponseEntity(expected, HttpStatus.CREATED)).`when`(examService).addExam(expected)
        val result = examController.addExam(expected)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.CREATED), result)
    }
}