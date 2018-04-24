package nl.han.ica.examplatform.controllers.exam

import nl.han.ica.examplatform.business.exam.ExamService
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*
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
        val expected = arrayOf(
                Exam("name-0", 10, Date(6000), "APP", ExamType.EXAM),
                Exam("name-1", 10, Date(6000), "APP", ExamType.EXAM))
        doReturn(expected
        ).`when`(examService).getExams()

        val result = examController.getExams()
        assertNotNull(result)
        assertArrayEquals(expected, result)
    }

    @Test
    fun testAddExam() {
        val expected = Exam("name-0", 10, Date(6000), "APP", ExamType.EXAM)
        doReturn(ResponseEntity(expected, HttpStatus.CREATED)).`when`(examService).addExam(expected)
        val result = examController.addExam(expected)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.CREATED), result)
    }
}