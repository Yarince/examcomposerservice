package nl.han.ica.examplatform.controllers.answer

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.models.ErrorInfo
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.EnumSet
import java.util.Locale
import java.util.logging.Logger

/**
 * Answer Controller.
 */
@ControllerAdvice(assignableTypes = [AnswerController::class])
class AnswerControllerAdvice : ResponseEntityExceptionHandler() {

    companion object {
        val logger = loggerFor(AnswerControllerAdvice::class.java)
    }

    /**
     * Handles Exeptions thrown by the [AnswerController].
     * Creates custom [ErrorInfo] class to be return to the API user.
     * Adds mandatory error headers to the error response.
     *
     * @return [ErrorInfo]
     */
    @ExceptionHandler(RuntimeException::class)
    @ResponseBody
    fun handleInvalidAnswerException(exception: Throwable): ResponseEntity<ErrorInfo> {
        val errorInfo = ErrorInfo(
                developerMessage = exception.message.orEmpty(),
                userMessage = "Answer could not be added to the Question")

        // RFC_1123_DATE_TIME  time format 'Tue, 3 Jun 2008 11:05:30 GMT'
        val date = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME)
        val status = HttpStatus.BAD_REQUEST

        val headers = HttpHeaders()
        headers.add("Date", date)
        headers.add("Server", "ExamComposeService")
        headers.add("Status", "${status.value()}  ${status.reasonPhrase}")
        headers.allow = EnumSet.of(HttpMethod.PUT)
        headers.contentLanguage = Locale.ENGLISH
        headers.contentType = MediaType.APPLICATION_JSON

        logger.error("Request failed 'handleInvalidAnswerException'")

        return ResponseEntity(errorInfo, headers, status)
    }
}
