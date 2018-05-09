package nl.han.ica.examplatform.business.exam

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import nl.han.ica.examplatform.controllers.responseexceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.exam.OfficialExam
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
internal class OfficialExamServiceTest {

    @InjectMocks
    private lateinit var officialExamService: OfficialExamService

    @Mock
    private lateinit var examDAO: ExamDAO

    @Test(expected = InvalidExamException::class)
    fun testCheckExamEmptyId() {
        val exam = OfficialExam(5, "name-0", 10, Date(6000), courseId = 1,
                version = 1,
                examType = ExamType.EXAM) // Faulty exam object

        officialExamService.checkExam(exam)
    }

    @Test
    fun testGetExams() {
        val expected = arrayListOf(SimpleExam(1, "SWA Toets 1", "SWA"),
                SimpleExam(2, "SWA Toets 2", "SWA"),
                SimpleExam(3, "APP Toets algoritmen", "APP")
        )

        doReturn(expected).`when`(examDAO).getExams()

        val result = officialExamService.getExams()
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }

    @Test(expected = InvalidExamException::class)
    fun testCheckExamEmptyQuestions() {
        val exam = OfficialExam(null, "name-0", 10, Date(6000), courseId = 1, version = 1, examType = ExamType.EXAM,
                questions = arrayListOf(Question())) // Faulty exam object
        officialExamService.checkExam(exam)
    }

    @Test
    fun testCheckExamNoException() {
        val exam = OfficialExam(null, "name-0", 10, Date(6000), courseId = 1, version = 1, examType = ExamType.EXAM)
        officialExamService.checkExam(exam)
    }

    @Test
    fun testAddExam() {
        val examInserted = OfficialExam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), courseId = 1, examType = ExamType.EXAM)
        val expectedResult = ResponseEntity(examInserted, HttpStatus.CREATED)

        doReturn(examInserted).`when`(examDAO).insertExam(examInserted)
        val result = officialExamService.addExam(examInserted)
        assertEquals(expectedResult, result)
    }

    @Test
    fun testGetExam() {
        val idOfExamToGet = 1
        val expected = OfficialExam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), courseId = 1, examType = ExamType.EXAM, examId = idOfExamToGet)
        doReturn(expected).`when`(examDAO).getExam(idOfExamToGet)
        val result = officialExamService.getExam(idOfExamToGet)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }
}