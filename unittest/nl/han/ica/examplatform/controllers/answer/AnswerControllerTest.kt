package nl.han.ica.examplatform.controllers.answer

import nl.han.ica.examplatform.exceptions.ErrorInfo
import nl.han.ica.examplatform.exceptions.answerExceptions.CouldNotAddAnswerToQuestionException
import nl.han.ica.examplatform.exceptions.answerExceptions.InvalidAnswerException
import nl.han.ica.examplatform.models.answer.Keywords
import nl.han.ica.examplatform.models.answer.OpenAnswer
import nl.han.ica.examplatform.business.answer.AnswerService
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
internal class AnswerControllerTest {

    @InjectMocks
    private lateinit var answerController: AnswerController

    @Mock
    private lateinit var answerService: AnswerService

    private val openAnswer: OpenAnswer = OpenAnswer(
            5,
            "dir",
            "com",
            Keywords(arrayOf("key"))
    )

    @Test
    fun testAddOpenAnswerToQuestion() {
        val result = answerController.addOpenAnswerToQuestion(openAnswer)
        Assert.assertNotNull(result)
        assertEquals(HttpStatus.OK, result)
    }

    @Test(expected = InvalidAnswerException::class)
    fun testAddOpenAnswerToQuestionInvalidAnswerException() {
        Mockito.doThrow(IllegalArgumentException("DAO Exception")).`when`(answerService).addAnswerToQuestion(openAnswer)
        answerController.addOpenAnswerToQuestion(openAnswer)
    }

    @Test(expected = CouldNotAddAnswerToQuestionException::class)
    fun testAddOpenAnswerToQuestionCouldNotAddAnswerToQuestionException() {
        val error = ErrorInfo(
                developerMessage = "DAO Exception",
                userMessage = "DAO Exception"
        )
        Mockito.doThrow(CouldNotAddAnswerToQuestionException(error)).`when`(answerService).addAnswerToQuestion(openAnswer)
        answerController.addOpenAnswerToQuestion(openAnswer)
    }

    @Test(expected = RuntimeException::class)
    fun testAddOpenAnswerToQuestionRuntimeException() {
        Mockito.doThrow(RuntimeException("DAO Exception")).`when`(answerService).addAnswerToQuestion(openAnswer)
        answerController.addOpenAnswerToQuestion(openAnswer)
    }
}