package nl.han.ica.examplatform.business.exam

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import nl.han.ica.examplatform.controllers.responseexceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.exam.ExamDAO
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*


@RunWith(MockitoJUnitRunner::class)
internal class ExamServiceTest {

    @InjectMocks
    private lateinit var examService: ExamService

    @Mock
    private lateinit var examDAO: ExamDAO

    @Test(expected = InvalidExamException::class)
    fun testCheckExamEmptyId() {
        val exam = Exam(5, "name-0", 10, Date(6000), courseId = 1,
                version = 1,
                examType = ExamType.EXAM) // Faulty exam object

        examService.checkExam(exam)
    }

    @Test
    fun testGetExams() {
        val expected = arrayListOf(SimpleExam(1, "SWA Toets 1", 1),
                SimpleExam(2, "SWA Toets 2", 1),
                SimpleExam(3, "APP Toets algoritmen", 2)
        )

        doReturn(expected).`when`(examDAO).getExams()

        val result = examService.getExams()
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }

    @Test(expected = InvalidExamException::class)
    fun testCheckExamEmptyQuestions() {
        val exam = Exam(null, "name-0", 10, Date(6000), courseId = 1, version = 1, examType = ExamType.EXAM,
                questions = arrayListOf(Question(questionPoints = 1F))) // Faulty exam object
        examService.checkExam(exam)
    }

    @Test
    fun testCheckExamNoException() {
        val exam = Exam(null, "name-0", 10, Date(6000), courseId = 1, version = 1, examType = ExamType.EXAM)
        examService.checkExam(exam)
    }

    @Test
    fun testAddExam() {
        val examInserted = Exam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), courseId = 1, examType = ExamType.EXAM)
        val expectedResult = ResponseEntity(examInserted, HttpStatus.CREATED)

        doReturn(examInserted).`when`(examDAO).insertExam(examInserted)
        val result = examService.addExam(examInserted)
        assertEquals(expectedResult, result)
    }

    @Test
    fun testGetExam() {
        val idOfExamToGet = 1
        val expected = Exam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), courseId = 1, examType = ExamType.EXAM, examId = idOfExamToGet)
        doReturn(expected).`when`(examDAO).getExam(idOfExamToGet)
        val result = examService.getExam(idOfExamToGet)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }
}