package nl.han.ica.examplatform.controllers

import nl.han.ica.examplatform.models.ErrorInfo
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.sql.SQLException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Creates custom [ErrorInfo] class to be return to the API user.
 * Adds mandatory error headers to the error response.
 */
@ControllerAdvice(value = ["nl.han.ica.examenplatform.business"])
abstract class BaseControllerAdvice : ResponseEntityExceptionHandler() {

    /**
     * Crates a ResponseEntity for the given Exception.
     *
     * @param httpStatus [HttpStatus]
     * @param userMessage [String]
     * @param exception [Throwable]
     * @return [ResponseEntity]<[ErrorInfo]>
     */
    fun createResponseEntity(
            httpStatus: HttpStatus,
            userMessage: String,
            exception: Throwable
    ): ResponseEntity<ErrorInfo> = createResponseEntity(
            httpStatus,
            userMessage,
            exception.message.orEmpty(),
            null,
            null)

    private fun createResponseEntity(
            httpStatus: HttpStatus,
            userMessage: String,
            devMessage: String,
            errorCode: String?,
            moreInfo: String?
    ): ResponseEntity<ErrorInfo> = ResponseEntity(
            ErrorInfo(
                    developerMessage = devMessage,
                    userMessage = userMessage,
                    errorCode = errorCode,
                    moreInfo = moreInfo),
            defaultHeaders(httpStatus),
            httpStatus)

    private fun defaultHeaders(httpStatus: HttpStatus): HttpHeaders {
        // RFC_1123_DATE_TIME  time format 'Tue, 3 Jun 2008 11:05:30 GMT'
        val date = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME)

        val headers = HttpHeaders()
        headers.add("Date", date)
        headers.add("Server", "ExamComposeService")
        headers.add("Status", "${httpStatus.value()}  ${httpStatus.reasonPhrase}")
        headers.contentLanguage = Locale.ENGLISH
        headers.contentType = MediaType.APPLICATION_JSON

        return headers
    }

    /**
     * Crates a ResponseEntity for [SQLException]s.
     *
     * @param e [SQLException]
     * @return [ResponseEntity]<[ErrorInfo]>
     */
    @ResponseBody
    @ExceptionHandler(SQLException::class)
    fun handleSQLException(
            e: SQLException,
            devMessage: String? = null
    ): ResponseEntity<ErrorInfo> = createResponseEntity(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Something went wrong.",
            devMessage ?: "Something went wrong with the database.",
            "SQL state error code: ${e.sqlState}",
            "https://en.wikipedia.org/wiki/SQLSTATE"
    )

    /**
     * Crates a ResponseEntity for [DatabaseException]s.
     *
     * @param e [DatabaseException]
     * @return [ResponseEntity]<[ErrorInfo]>
     */
    @ResponseBody
    @ExceptionHandler(DatabaseException::class)
    fun handleDatabaseException(e: DatabaseException): ResponseEntity<ErrorInfo> {
        return if (e.cause != null) {
            handleSQLException(e.cause, e.message)
        } else {
            createResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong.",
                    e.message ?: "Something went wrong with the database.",
                    null,
                    null
            )
        }
    }
}
