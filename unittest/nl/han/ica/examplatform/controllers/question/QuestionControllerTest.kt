package nl.han.ica.examplatform.controllers.question

import nl.han.ica.examplatform.business.question.QuestionService
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.models.question.QuestionType
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class QuestionControllerTest {

    @InjectMocks
    private
    lateinit var questionController: QuestionController

    @Mock
    private
    lateinit var questionService: QuestionService

    @Test
    fun testCreateQuestion() {
        val expectedResultBody = Question(questionId = 0, questionOrderInExam = 1, questionText = "name", questionType = QuestionType.OPEN_QUESTION, questionPoints = 5F)
        val expectedStatusCode = HttpStatus.CREATED

        doReturn(ResponseEntity(expectedResultBody, expectedStatusCode)).`when`(questionService).addQuestion(expectedResultBody)

        val result = questionController.createQuestion(expectedResultBody)

        assertNotNull(result)
        assertEquals(expectedResultBody, result.body)
        assertEquals(expectedStatusCode, result.statusCode)
    }
}