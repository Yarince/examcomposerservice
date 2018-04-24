package nl.han.ica.examplatform.controllers.exam

import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.business.exam.ExamService
import nl.han.ica.examplatform.business.examquestion.ExamQuestionService
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.models.question.QuestionType
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

    @Mock
    private
    lateinit var examQuestionService: ExamQuestionService

    @Test
    fun testAddExam() {
        val expected = Exam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), course = "APP", version = 1, examType = ExamType.EXAM)
        doReturn(ResponseEntity(expected, HttpStatus.CREATED)).`when`(examService).addExam(expected)
        val result = examController.addExam(expected)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.CREATED), result)
    }

    @Test
    fun addQuestionToExam() {
        val expected = Exam(examId = 1, name = "name-0", durationInMinutes = 10, startTime = Date(6000), course = "APP", version = 1, examType = ExamType.EXAM, questions = Array(1, {
            Question(1, "Text", QuestionType.OPEN_QUESTION, "Course", null, ExamType.EXAM)
        }))

        doReturn(ResponseEntity(expected, HttpStatus.ACCEPTED)).`when`(examQuestionService).addQuestionToExam(expected)
        val result = examController.addQuestionToExam(expected)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.ACCEPTED), result)
    }
}