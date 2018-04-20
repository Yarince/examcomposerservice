package nl.han.ica.examplatform.controllers.exam

import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.business.exam.ExamService
import nl.han.ica.examplatform.models.exam.SimpleExam
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.Date
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
        val expected = arrayOf(SimpleExam(1, "SWA Toets 1", "SWA"),
                SimpleExam(2, "SWA Toets 2", "SWA"),
                SimpleExam(3, "APP Toets algoritmen", "APP")
        )
        doReturn(ResponseEntity(expected, HttpStatus.OK)).`when`(examService).getExams()

        val result = examController.getExams()
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }

    @Test
    fun testAddExam() {
        val expected = Exam("name-0", 10, Date(6000), "APP", ExamType.EXAM)
        doReturn(ResponseEntity(expected, HttpStatus.CREATED)).`when`(examService).addExam(expected)
        val result = examController.addExam(expected)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.CREATED), result)
    }

    @Test
    fun testGetExam() {
        val idOfExamToGet = 1
        val expected = Exam("name-0", 10, Date(6000), "APP", ExamType.EXAM, examId = idOfExamToGet)
        doReturn(ResponseEntity(expected, HttpStatus.OK)).`when`(examService).getExam(idOfExamToGet)
        val result = examController.getExam(idOfExamToGet)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }
}