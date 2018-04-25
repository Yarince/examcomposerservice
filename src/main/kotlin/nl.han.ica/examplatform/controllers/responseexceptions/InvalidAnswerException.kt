package nl.han.ica.examplatform.controllers.responseexceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class InvalidAnswerException(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
) : RuntimeException(message, cause, enableSuppression, writableStackTrace)