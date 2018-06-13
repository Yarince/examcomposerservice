package nl.han.ica.examplatform.controllers.answer

import nl.han.ica.examplatform.business.answer.AnswerService
import nl.han.ica.examplatform.business.answer.CouldNotAddAnswerToQuestionException
import nl.han.ica.examplatform.models.answermodel.answer.Answer
import nl.han.ica.examplatform.models.answermodel.answer.PartialAnswer
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
class AnswerControllerTest {

    @InjectMocks
    private lateinit var answerController: AnswerController

    @Mock
    private lateinit var answerService: AnswerService

    private val answer: Answer = Answer(
            questionId = 5,
            partial_answers = arrayListOf(PartialAnswer(id = 1, text = "sad", points = 7))
    )

    @Test
    fun testAddAnswerToQuestion() {
        val result = answerController.addAnswerToQuestion(answer)
        Assert.assertNotNull(result)
        assertEquals(HttpStatus.CREATED, result)
    }

    @Test(expected = InvalidAnswerException::class)
    fun testAddAnswerToQuestionInvalidAnswerException() {
        Mockito.doThrow(IllegalArgumentException("DAO Exception")).`when`(answerService).addOrUpdateAnswerInQuestion(answer)
        answerController.addAnswerToQuestion(answer)
    }

    @Test(expected = CouldNotAddAnswerToQuestionException::class)
    fun testAddAnswerToQuestionCouldNotAddAnswerToQuestionException() {
        Mockito.doThrow(CouldNotAddAnswerToQuestionException("message", null)).`when`(answerService).addOrUpdateAnswerInQuestion(answer)
        answerController.addAnswerToQuestion(answer)
    }

    @Test(expected = RuntimeException::class)
    fun testAddAnswerToQuestionRuntimeException() {
        Mockito.doThrow(RuntimeException("DAO Exception")).`when`(answerService).addOrUpdateAnswerInQuestion(answer)
        answerController.addAnswerToQuestion(answer)
    }
}