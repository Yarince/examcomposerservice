package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.controllers.responseExceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.exam.ExamDAOStub
import org.junit.Assert.*

import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*


@RunWith(MockitoJUnitRunner::class)
internal class ExamServiceTest {

    @InjectMocks
    private
    lateinit var examService: ExamService

    @Mock
    private
    lateinit var examDAO: ExamDAOStub

    @Test
    fun validateEmptyId() {
        val exam = Exam(5, "name-0", 10, Date(6000), course = "APP",
                version = 1,
                examType = ExamType.EXAM) // Faulty exam object

        Assertions.assertThrows(InvalidExamException::class.java) {
            examService.validateEmptyId(exam)
        }
    }

    @Test
    fun testValidateEmptyQuestions() {
        val exam = Exam(5, "name-0", 10, Date(6000), course = "APP", version = 1, examType = ExamType.EXAM,
                questions = Array(1, { Question() })) // Faulty exam object
        Assertions.assertThrows(InvalidExamException::class.java) {
            examService.validateEmptyQuestions(exam)
        }
    }

    @Test
    fun getExams() {
        val expected = arrayOf(
                Exam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), course = "APP", version = 1, examType = ExamType.EXAM),
                Exam(name = "name-1", durationInMinutes = 10, startTime = Date(6000), course = "APP", version = 1, examType = ExamType.EXAM))

        doReturn(expected).`when`(examDAO).getAllExams()


        val result = examService.getExams()
        assertArrayEquals(expected, result)
    }

    @Test
    fun addExam() {
        val examInserted = Exam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), course = "APP", examType = ExamType.EXAM)
        val expectedResult = ResponseEntity(examInserted, HttpStatus.CREATED)

        doReturn(examInserted).`when`(examDAO).insertExam(examInserted)

        val result = examService.addExam(examInserted)
        assertEquals(expectedResult, result)
    }
}