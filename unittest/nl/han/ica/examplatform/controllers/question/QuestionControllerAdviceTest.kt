package nl.han.ica.examplatform.controllers.question

import junit.framework.Assert
import nl.han.ica.examplatform.business.question.QuestionNotInsertedException
import nl.han.ica.examplatform.models.ErrorInfo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@RunWith(MockitoJUnitRunner::class)
class QuestionControllerAdviceTest {

    private val questionControllerAdvice: QuestionControllerAdvice = QuestionControllerAdvice()
    private val questionNotFoundException: QuestionNotFoundException =
            QuestionNotFoundException("No Question Found.")
    private val questionNotInsertedException: QuestionNotInsertedException =
            QuestionNotInsertedException("Question Not Inserted.")

    @Test
    fun testQuestionNotFoundException(){
        val expectedErrorInfo = ErrorInfo(
                developerMessage = "No Question Found.",
                userMessage = "Requested Question is not available."
        )

        val response: ResponseEntity<ErrorInfo> = questionControllerAdvice
                .handleQuestionNotFoundException(questionNotFoundException)

        assertResponse(response, expectedErrorInfo, HttpStatus.NOT_FOUND)
    }

    @Test
    fun testQuestionNotInsertedException(){
        val expectedErrorInfo = ErrorInfo(
                developerMessage = "Question Not Inserted.",
                userMessage = "Question has not been created."
        )

        val response: ResponseEntity<ErrorInfo> = questionControllerAdvice
                .handleQuestionNotInsertedException(questionNotInsertedException)

        assertResponse(response, expectedErrorInfo, HttpStatus.INTERNAL_SERVER_ERROR)

    }

    private fun assertResponse(
            response: ResponseEntity<ErrorInfo>,
            expectedErrorInfo: ErrorInfo,
            expectedHttpStatus: HttpStatus
    ) {
        Assert.assertNotNull(response)
        Assert.assertNotNull(response.body)
        Assert.assertNotNull(response.statusCode)
        Assert.assertEquals(expectedErrorInfo.developerMessage, response.body?.developerMessage)
        Assert.assertEquals(expectedErrorInfo.userMessage, response.body?.userMessage)
        Assert.assertEquals(expectedErrorInfo.errorCode, response.body?.errorCode)
        Assert.assertEquals(expectedHttpStatus, response.statusCode)
    }

}