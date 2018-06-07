package nl.han.ica.examplatform.controllers.exam

import nl.han.ica.examplatform.controllers.BaseControllerAdvice
import nl.han.ica.examplatform.models.ErrorInfo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Handles Exceptions thrown by the [ExamController].
 */
@ControllerAdvice(assignableTypes = [ExamController::class])
class ExamControllerAdvice : BaseControllerAdvice() {

    /**
     * Handles [InvalidExamException].
     *
     * @return [ErrorInfo]
     */
    @ResponseBody
    @ExceptionHandler(InvalidExamException::class)
    fun handleInvalidAnswerException(e: InvalidExamException): ResponseEntity<Any> =
            createResponseEntity(HttpStatus.BAD_REQUEST, "Given Exam is not correct.", e)

    /**
     * Handles [ExamNotFoundException].
     *
     * @return [ErrorInfo]
     */
    @ResponseBody
    @ExceptionHandler(ExamNotFoundException::class)
    fun handleExamNotFoundException(e: ExamNotFoundException): ResponseEntity<Any> =
            createResponseEntity(HttpStatus.NOT_FOUND, "Requested Exam has not been found.", e)

}