package nl.han.ica.examplatform.service.answer

import com.nhaarman.mockito_kotlin.mock
import nl.han.ica.examplatform.business.answer.AnswerService
import nl.han.ica.examplatform.exceptions.answerExceptions.CouldNotAddAnswerToQuestionException
import nl.han.ica.examplatform.models.answer.Answer
import nl.han.ica.examplatform.persistence.answer.AnswerDAO
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
internal class AnswerServiceTest {

    @InjectMocks
    private lateinit var answerService: AnswerService

    @Mock
    private lateinit var answerDAO: AnswerDAO

    @Test(expected = CouldNotAddAnswerToQuestionException::class)
    fun testAddAnswerToQuestionError() {
        val answer = mock<Answer>()
        Mockito.doThrow(RuntimeException("DAO Exception")).`when`(answerDAO).addAnswerToQuestion(answer)
        answerService.addAnswerToQuestion(answer)
    }
}