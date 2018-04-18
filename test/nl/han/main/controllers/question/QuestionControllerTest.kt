package nl.han.main.controllers.question

import nl.han.main.model.question.ExamType
import nl.han.main.model.question.Question
import nl.han.main.model.question.QuestionType
import nl.han.main.service.question.QuestionService
import org.junit.Assert.*
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
        val expectedResultBody = Question(1, "", QuestionType.OPEN_QUESTION, "", null, ExamType.PRACTICE_EXAM, null)
        val expectedStatusCode = HttpStatus.CREATED

        doReturn(ResponseEntity(expectedResultBody, expectedStatusCode)).`when`(questionService).addQuestion(expectedResultBody)

        val result = questionController.createQuestion(expectedResultBody)

        assertNotNull(result)
        assertEquals(expectedResultBody, result.body)
        assertEquals(expectedStatusCode, result.statusCode)
    }
}