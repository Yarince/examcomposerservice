package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.controllers.responseexceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.exam.ExamDAOStub
import junit.framework.TestCase.assertEquals

import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Test
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

    @Test(expected = InvalidExamException::class)
    fun testCheckExamEmptyId() {
        val exam = Exam(5, "name-0", 10, Date(6000), course = "APP",
                version = 1,
                examType = ExamType.EXAM) // Faulty exam object

        examService.checkExam(exam)
    }

    @Test(expected = InvalidExamException::class)
    fun testCheckExamEmptyQuestions() {
        val exam = Exam(null, "name-0", 10, Date(6000), course = "APP", version = 1, examType = ExamType.EXAM,
                questions = arrayOf(Question())) // Faulty exam object
        examService.checkExam(exam)
    }

    @Test
    fun testCheckExamNoException() {
        val exam = Exam(null, "name-0", 10, Date(6000), course = "APP", version = 1, examType = ExamType.EXAM)
        examService.checkExam(exam)
    }

    @Test
    fun addExam() {
        val examInserted = Exam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), course = "APP", examType = ExamType.EXAM
                , questions = null)
        val expectedResult = ResponseEntity(examInserted, HttpStatus.CREATED)

        doReturn(examInserted).`when`(examDAO).insertExam(examInserted)

        val result = examService.addExam(examInserted)
        assertEquals(expectedResult, result)
    }
}