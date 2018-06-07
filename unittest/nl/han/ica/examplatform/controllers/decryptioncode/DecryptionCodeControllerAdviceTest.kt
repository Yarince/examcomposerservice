package nl.han.ica.examplatform.controllers.decryptioncode

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import nl.han.ica.examplatform.models.ErrorInfo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@RunWith(MockitoJUnitRunner::class)
class DecryptionCodeControllerAdviceTest {

    private val answerControllerAdvice: DecryptionCodeControllerAdvice = DecryptionCodeControllerAdvice()
    private val decryptionCodeNotFoundException: DecryptionCodeNotFoundException = DecryptionCodeNotFoundException()

    @Test
    fun testHandleInvalidAnswerException() {
        val expectedErrorInfo = ErrorInfo(
                developerMessage = """
                    |There is no Decryption Code in the Database for the given exam.
                    |Maybe the EFTS did not yet prepare this exam.
                    |""".trimMargin(),
                userMessage = "No Decryption Code found for the given exam."
        )

        val response: ResponseEntity<Any> = answerControllerAdvice
                .handleDecryptionCodeNotFoundException(decryptionCodeNotFoundException)

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