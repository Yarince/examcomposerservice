package nl.han.ica.examplatform.controllers

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import nl.han.ica.examplatform.models.ErrorInfo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.sql.SQLException

@RunWith(MockitoJUnitRunner::class)
class BaseControllerAdviceTest {

    private val baseControllerAdvice: BaseControllerAdvice = Mockito.mock(
            BaseControllerAdvice::class.java, Mockito.CALLS_REAL_METHODS)

    @Test
    fun testHandleDatabaseException() {
        val databaseException = DatabaseException("exception")
        val expectedErrorInfo = ErrorInfo(
                developerMessage = "exception",
                userMessage = "Something went wrong."
        )

        val response: ResponseEntity<Any> = baseControllerAdvice
                .handleDatabaseException(databaseException)
        assertResponse(response, expectedErrorInfo, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun testHandleDatabaseExceptionWithSQLException() {
        val databaseException = DatabaseException("exception", SQLException("reason", "state", 0, null))
        val expectedErrorInfo = ErrorInfo(
                developerMessage = "exception",
                userMessage = "Something went wrong.",
                errorCode = "SQL state error code: state",
                moreInfo = "https://en.wikipedia.org/wiki/SQLSTATE"
        )

        val response: ResponseEntity<Any> = baseControllerAdvice
                .handleDatabaseException(databaseException)
        assertResponse(response, expectedErrorInfo, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun testHandleSQLException() {
        val sqlException = SQLException("reason", "state", 0, null)
        val expectedErrorInfo = ErrorInfo(
                developerMessage = "Something went wrong with the database.",
                userMessage = "Something went wrong.",
                errorCode = "SQL state error code: state",
                moreInfo = "https://en.wikipedia.org/wiki/SQLSTATE"
        )

        val response: ResponseEntity<Any> = baseControllerAdvice
                .handleSQLException(sqlException)

        assertResponse(response, expectedErrorInfo, HttpStatus.INTERNAL_SERVER_ERROR)
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