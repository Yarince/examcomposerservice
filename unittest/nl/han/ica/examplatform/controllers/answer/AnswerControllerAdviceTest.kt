package nl.han.ica.examplatform.controllers.answer

import nl.han.ica.examplatform.models.ErrorInfo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(MockitoJUnitRunner::class)
class AnswerControllerAdviceTest {

    private val answerControllerAdvice: AnswerControllerAdvice = AnswerControllerAdvice()
    private val throwable: Throwable = Throwable("Answer contains invalid values")

    @Test
    fun testHandleInvalidAnswerException() {
        val expectedErrorInfo = ErrorInfo(
                developerMessage = "Answer contains invalid values",
                userMessage = "Answer could not be added tot the question"
        )

        val response = answerControllerAdvice
                .handleInvalidAnswerException(throwable)
        assertNotNull(response)
        assertNotNull(response.body)
        assertNotNull(response.statusCode)
        assertEquals(expectedErrorInfo.developerMessage, response.body?.developerMessage)
        assertEquals(expectedErrorInfo.userMessage, response.body?.userMessage)
        assertEquals(expectedErrorInfo.errorCode, response.body?.errorCode)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }
}