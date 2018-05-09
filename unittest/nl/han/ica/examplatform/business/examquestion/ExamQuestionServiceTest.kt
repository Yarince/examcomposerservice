package nl.han.ica.examplatform.business.examquestion

import nl.han.ica.examplatform.controllers.responseexceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.models.question.QuestionType
import nl.han.ica.examplatform.persistence.exam.ExamDAO
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class ExamQuestionServiceTest {

    @InjectMocks
    lateinit var examQuestionService: ExamQuestionService

    @Mock
    lateinit var examDAO: ExamDAO

    @Mock
    lateinit var questionDAO: QuestionDAO

    @Test
    fun testCheckQuestion() {
        val question = Question(courseId = 1)
        doReturn(true).`when`(questionDAO).exists(question)
        examQuestionService.checkQuestion(arrayListOf(question))
        verify(questionDAO, times(1)).exists(question)
    }

    @Test(expected = InvalidExamException::class)
    fun testCheckQuestionEmpty() {
        examQuestionService.checkQuestion(null)
    }


    @Test(expected = InvalidExamException::class)
    fun testCheckQuestionNotExisting() {
        val question = Question(courseId = 1)
        doReturn(false).`when`(questionDAO).exists(question)
        examQuestionService.checkQuestion(arrayListOf(question))
    }

    @Test
    fun testAddQuestionToExam() {
        val expectedQuestion = Question(courseId = 1, examTypeId = ExamType.EXAM, questionType = QuestionType.OPEN_QUESTION)
        val expectedExam = Exam(examId = 1, name = "name-0", durationInMinutes = 10, startTime = Date(6000), courseId = 1, version = 1, examType = ExamType.EXAM, questions = arrayListOf(expectedQuestion))

        doReturn(expectedExam).`when`(examDAO).addQuestionsToExam(expectedExam)
        doReturn(true).`when`(questionDAO).exists(expectedQuestion)

        assertEquals(ResponseEntity(expectedExam, HttpStatus.ACCEPTED), examQuestionService.addQuestionToExam(expectedExam))
    }
}