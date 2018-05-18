package nl.han.ica.examplatform.controllers.answer

import nl.han.ica.examplatform.business.answer.AnswerService
import nl.han.ica.examplatform.controllers.responseexceptions.CouldNotAddAnswerToQuestionException
import nl.han.ica.examplatform.controllers.responseexceptions.InvalidAnswerException
import nl.han.ica.examplatform.models.answerModel.answer.Answer
import nl.han.ica.examplatform.models.answerModel.answer.Keyword
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

    private val answer: Answer = Answer(
            5,
            arrayOf(Keyword("sad", 7.7F))
    )

    @Test
    fun testAddAnswerToQuestion() {
        val result = answerController.addAnswerToQuestion(answer)
        Assert.assertNotNull(result)
        assertEquals(HttpStatus.OK, result)
    }

    @Test(expected = InvalidAnswerException::class)
    fun testAddAnswerToQuestionInvalidAnswerException() {
        Mockito.doThrow(IllegalArgumentException("DAO Exception")).`when`(answerService).addAnswerToQuestion(answer)
        answerController.addAnswerToQuestion(answer)
    }

    @Test(expected = CouldNotAddAnswerToQuestionException::class)
    fun testAddAnswerToQuestionCouldNotAddAnswerToQuestionException() {
        Mockito.doThrow(CouldNotAddAnswerToQuestionException("message", null, false, false)).`when`(answerService).addAnswerToQuestion(answer)
        answerController.addAnswerToQuestion(answer)
    }

    @Test(expected = RuntimeException::class)
    fun testAddAnswerToQuestionRuntimeException() {
        Mockito.doThrow(RuntimeException("DAO Exception")).`when`(answerService).addAnswerToQuestion(answer)
        answerController.addAnswerToQuestion(answer)
    }
}