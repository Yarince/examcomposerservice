package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.persistence.exam.ExamDAOStub
import org.junit.Assert.*

import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*


@RunWith(MockitoJUnitRunner::class)
internal class ExamServiceTest {

    @InjectMocks
    lateinit var examService: ExamService

    @Mock
    private
    lateinit var examDAO: ExamDAOStub

    @Test
    fun testGetExams() {
        val expected = arrayOf(SimpleExam(1, "SWA Toets 1", "SWA"),
                SimpleExam(2, "SWA Toets 2", "SWA"),
                SimpleExam(3, "APP Toets algoritmen", "APP")
        )

        val result = examService.getExams()
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }

    @Test
    fun testAddExam() {
        val expected = Exam("name-0", 10, Date(6000), "APP", ExamType.EXAM)
        Mockito.doReturn(expected).`when`(examDAO).insertExam(expected)
        val result = examService.addExam(expected)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.CREATED), result)
    }

    @Test
    fun testGetExam() {
        val idOfExamToGet = 1
        val expected = Exam("name-0", 10, Date(6000), "APP", ExamType.EXAM, examId = idOfExamToGet)
        Mockito.doReturn(expected).`when`(examDAO).getExam(idOfExamToGet)
        val result = examService.getExam(idOfExamToGet)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }
}