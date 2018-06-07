package nl.han.ica.examplatform.controllers.answer

import nl.han.ica.examplatform.controllers.BaseControllerAdvice
import nl.han.ica.examplatform.models.ErrorInfo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Handles Exceptions thrown by the [AnswerController].
 */
@ControllerAdvice(assignableTypes = [AnswerController::class])
class AnswerControllerAdvice : BaseControllerAdvice() {

    /**
     * Handles InvalidAnswerException thrown by the [AnswerController].
     *
     * @return [ErrorInfo]
     */
    @ResponseBody
    @ExceptionHandler(InvalidAnswerException::class)
    fun handleInvalidAnswerException(e: InvalidAnswerException): ResponseEntity<Any> =
            createResponseEntity(HttpStatus.BAD_REQUEST, "Answer could not be added to the Question", e)
}
