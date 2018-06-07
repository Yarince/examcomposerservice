package nl.han.ica.examplatform.controllers.decryptioncode

import nl.han.ica.examplatform.controllers.BaseControllerAdvice
import nl.han.ica.examplatform.models.ErrorInfo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Handles Exceptions thrown by the [DecryptionCodeController].
 */
@ControllerAdvice(assignableTypes = [DecryptionCodeController::class])
class DecryptionCodeControllerAdvice : BaseControllerAdvice() {

    /**
     * Handles [DecryptionCodeNotFoundException].
     *
     * @return [ErrorInfo]
     */
    @ResponseBody
    @ExceptionHandler(DecryptionCodeNotFoundException::class)
    fun handleDecryptionCodeNotFoundException(e: DecryptionCodeNotFoundException): ResponseEntity<Any> =
            createResponseEntity(HttpStatus.NOT_FOUND, "No Decryption Code found for the given exam.", e)
}