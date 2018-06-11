package nl.han.ica.examplatform.controllers.question

import nl.han.ica.examplatform.business.question.QuestionNotInsertedException
import nl.han.ica.examplatform.controllers.BaseControllerAdvice
import nl.han.ica.examplatform.models.ErrorInfo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice(assignableTypes = [QuestionController::class])
class QuestionControllerAdvice : BaseControllerAdvice() {

    /**
     * Handles [QuestionNotFoundException].
     *
     * @param e [QuestionNotFoundException]
     * @return [ErrorInfo]
     */
    @ResponseBody
    @ExceptionHandler(QuestionNotFoundException::class)
    fun handleQuestionNotFoundException(e: QuestionNotFoundException): ResponseEntity<Any> =
            createResponseEntity(HttpStatus.NOT_FOUND, "Requested Question is not available.", e)

    /**
     * Handles [QuestionNotInsertedException].
     *
     * @param e [QuestionNotInsertedException]
     * @return [ErrorInfo]
     */
    @ResponseBody
    @ExceptionHandler(QuestionNotInsertedException::class)
    fun handleQuestionNotInsertedException(e: QuestionNotInsertedException): ResponseEntity<Any> =
            createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Question has not been created.", e)

}