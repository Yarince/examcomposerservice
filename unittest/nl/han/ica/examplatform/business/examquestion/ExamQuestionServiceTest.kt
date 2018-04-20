package nl.han.ica.examplatform.business.examquestion

import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.models.question.QuestionType
import nl.han.ica.examplatform.persistence.exam.ExamDAOStub
import nl.han.ica.examplatform.persistence.question.QuestionDAOStub
import org.junit.Assert.*
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
class ExamQuestionServiceTest {

    @InjectMocks
    lateinit var examQuestionService: ExamQuestionService

    @Mock
    lateinit var examDAO: ExamDAOStub

    @Mock
    lateinit var questionDAO: QuestionDAOStub

    @Test
    fun checkQuestion() {
        val question = Question()
        doReturn(true).`when`(questionDAO).exists(question)
        assertTrue(examQuestionService.checkQuestion(Array(1, { question })))
    }

    @Test
    fun checkQuestionEmpty() {
        assertFalse(examQuestionService.checkQuestion(null))
    }


    @Test
    fun checkQuestionNotExisting() {
        val question = Question()
        doReturn(false).`when`(questionDAO).exists(question)
        assertFalse(examQuestionService.checkQuestion(Array(1, { question })))
    }

    @Test
    fun addQuestionToExam() {
        val expectedQuestion = Question(course = "APP", examType = ExamType.EXAM, questionType = QuestionType.OPEN_QUESTION)
        val expectedExam = Exam(examId = 1, name = "name-0", durationInMinutes = 10, startTime = Date(6000), course = "APP", version = 1, examType = ExamType.EXAM, questions = Array(1, { expectedQuestion }))

        doReturn(false).`when`(examDAO).updateExam(expectedExam)

        assertEquals(ResponseEntity(expectedExam, HttpStatus.ACCEPTED), examQuestionService.addQuestionToExam(expectedExam))
    }
}