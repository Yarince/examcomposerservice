package nl.han.ica.examplatform.controllers.answer

import nl.han.ica.examplatform.models.ErrorInfo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(MockitoJUnitRunner::class)
class AnswerControllerAdviceTest {

    private val answerControllerAdvice: AnswerControllerAdvice = AnswerControllerAdvice()
    private val invalidAnswerException: InvalidAnswerException = InvalidAnswerException("Answer contains invalid values")

    @Test
    fun testHandleInvalidAnswerException() {
        val expectedErrorInfo = ErrorInfo(
                developerMessage = "Answer contains invalid values",
                userMessage = "Answer could not be added to the Question"
        )

        val response: ResponseEntity<Any> = answerControllerAdvice
                .handleInvalidAnswerException(invalidAnswerException)

        assertResponse(response, expectedErrorInfo, HttpStatus.BAD_REQUEST)
    }

    private fun assertResponse(
            response: ResponseEntity<Any>,
            expectedErrorInfo: ErrorInfo,
            expectedHttpStatus: HttpStatus
    ) {
        assertNotNull(response)
        assertNotNull(response.body)
        assertNotNull(response.statusCode)
        assertEquals(expectedErrorInfo.toString(), response.body?.toString())
        assertEquals(expectedHttpStatus, response.statusCode)
    }
}