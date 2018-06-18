package nl.han.ica.examplatform.controllers.exam

import nl.han.ica.examplatform.business.exam.ExamService
import nl.han.ica.examplatform.business.examquestion.ExamQuestionService
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.PracticeExam
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.models.question.Question
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

    @Mock
    private
    lateinit var examQuestionService: ExamQuestionService

    @Test
    fun testGeneratePracticeExam() {
        val expected = PracticeExam(name = "Practice exam", courseId = 1, questions = arrayListOf())
        doReturn(ResponseEntity<Any>(expected, HttpStatus.CREATED)).`when`(examService).generatePersonalPracticeExam(1, 1)
        val result = examController.generatePracticeExam(1, 1)
        assertEquals(ResponseEntity(expected, HttpStatus.CREATED), result)
    }

    @Test
    fun testGetExams() {
        val expected = arrayListOf(SimpleExam(1, "SWA Toets 1", 1),
                SimpleExam(2, "SWA Toets 2", 1),
                SimpleExam(3, "APP Toets algoritmen", 1)
        )
        doReturn(ResponseEntity<Any>(expected, HttpStatus.OK)).`when`(examService).getExams()

        val result = examController.getExams()
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }

    @Test
    fun testAddExam() {
        val expected = Exam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), courseId = 1, version = 1, examType = "Tentamen")
        doReturn(ResponseEntity(expected, HttpStatus.CREATED)).`when`(examService).addExam(expected)
        val result = examController.addExam(expected)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.CREATED), result)
    }

    @Test
    fun addQuestionToExam() {
        val expected = Exam(examId = 1, name = "name-0", durationInMinutes = 10, startTime = Date(6000), courseId = 1, version = 1, examType = "Tentamen", questions = arrayListOf(
                Question(questionId = 1, questionType = "OpenQuestion", questionPoints = 5, courseId = 1, examType = "Tentamen", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", questionTypePluginVersion = "1.0", partial_answers = arrayListOf())))

        doReturn(ResponseEntity(expected, HttpStatus.ACCEPTED)).`when`(examQuestionService).addQuestionToExam(expected)
        val result = examController.addQuestionToExam(expected)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.ACCEPTED), result)
    }

    @Test
    fun testGetExam() {
        val idOfExamToGet = 1
        val expected = Exam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), courseId = 1, examType = "Tentamen", examId = idOfExamToGet)
        doReturn(ResponseEntity(expected, HttpStatus.OK)).`when`(examService).getExam(idOfExamToGet)
        val result = examController.getExam(idOfExamToGet)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }
}