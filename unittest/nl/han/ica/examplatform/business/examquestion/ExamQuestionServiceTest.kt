package nl.han.ica.examplatform.business.examquestion

import nl.han.ica.examplatform.controllers.exam.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.question.Question
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
        val question = Question(questionId = 1, questionType = "OpenQuestion", questionPoints = 4, courseId = 1, examType = "Tentamen", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", partialAnswers = arrayListOf())
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
        val question = Question(questionId = 1, questionType = "OpenQuestion", questionPoints = 4, courseId = 1, examType = "Tentamen", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", partialAnswers = arrayListOf())
        doReturn(false).`when`(questionDAO).exists(question)
        examQuestionService.checkQuestion(arrayListOf(question))
    }

    @Test
    fun testAddQuestionToExam() {
        val expectedQuestion = Question(questionId = 0, questionOrderInExam = 1, questionType = "OpenQuestion", questionText = "name", questionPoints = 5, courseId = 1, examType = "Tentamen", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", partialAnswers = arrayListOf())
        val expectedExam = Exam(examId = 1, name = "name-0", durationInMinutes = 10, startTime = Date(6000), courseId = 1, version = 1, examType = "Tentamen", questions = arrayListOf(expectedQuestion))

        doReturn(expectedExam).`when`(examDAO).addQuestionsToExam(expectedExam)
        doReturn(true).`when`(questionDAO).exists(expectedQuestion)

        assertEquals(ResponseEntity(expectedExam, HttpStatus.ACCEPTED), examQuestionService.addQuestionToExam(expectedExam))
    }
}