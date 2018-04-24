package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.controllers.responseExceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.persistence.exam.ExamDAOStub
import nl.han.ica.examplatform.models.question.Question
import org.junit.Assert.*

import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.junit.jupiter.api.Assertions
import org.mockito.Mockito.doReturn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*


@RunWith(MockitoJUnitRunner::class)
internal class ExamServiceTest {

    @InjectMocks
    private lateinit var examService: ExamService

    @Mock
    private lateinit var examDAO: ExamDAOStub
    @Test
    fun testCheckExamEmptyId() {
        val exam = Exam(5, "name-0", 10, Date(6000), course = "APP",
                version = 1,
                examType = ExamType.EXAM) // Faulty exam object

        Assertions.assertThrows(InvalidExamException::class.java) {
            examService.checkExam(exam)
        }
    }

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
    fun testCheckExamEmptyQuestions() {
        val exam = Exam(null, "name-0", 10, Date(6000), course = "APP", version = 1, examType = ExamType.EXAM,
                questions = arrayOf(Question())) // Faulty exam object
        Assertions.assertThrows(InvalidExamException::class.java) {
            examService.checkExam(exam)
        }
    }

    @Test
    fun testCheckExamNoException() {
        val exam = Exam(null, "name-0", 10, Date(6000), course = "APP", version = 1, examType = ExamType.EXAM)
        examService.checkExam(exam)
    }


    @Test
    fun testAddExam() {
        val examInserted = Exam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), course = "APP", examType = ExamType.EXAM
                , questions = null)
        val expectedResult = ResponseEntity(examInserted, HttpStatus.CREATED)

        doReturn(examInserted).`when`(examDAO).insertExam(examInserted)

        val result = examService.addExam(examInserted)
        assertEquals(expectedResult, result)
    }

    @Test
    fun testGetExam() {
        val idOfExamToGet = 1
        val expected = Exam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), course = "APP", examType = ExamType.EXAM, examId = idOfExamToGet)
        Mockito.doReturn(expected).`when`(examDAO).getExam(idOfExamToGet)
        val result = examService.getExam(idOfExamToGet)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }
}