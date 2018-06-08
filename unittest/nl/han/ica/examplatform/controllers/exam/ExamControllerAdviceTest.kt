package nl.han.ica.examplatform.controllers.exam

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import nl.han.ica.examplatform.models.ErrorInfo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@RunWith(MockitoJUnitRunner::class)
class ExamControllerAdviceTest {

    private val examControllerAdvice: ExamControllerAdvice = ExamControllerAdvice()
    private val examNotFoundException: ExamNotFoundException = ExamNotFoundException()
    private val invalidExamException: InvalidExamException = InvalidExamException()

    @Test
    fun testHandleExamNotFoundException() {
        val expectedErrorInfo = ErrorInfo(
                developerMessage = "The exam requested with the given id is not present in the database.",
                userMessage = "Requested Exam has not been found."
        )

        val response: ResponseEntity<Any> = examControllerAdvice
                .handleExamNotFoundException(examNotFoundException)

        assertResponse(response, expectedErrorInfo, HttpStatus.NOT_FOUND)
    }

    @Test
    fun testHandleInvalidExamException() {
        val expectedErrorInfo = ErrorInfo(
                developerMessage = "The given exam was contained invalid properties.",
                userMessage = "Given Exam is not correct."
        )

        val response: ResponseEntity<Any> = examControllerAdvice
                .handleInvalidAnswerException(invalidExamException)

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