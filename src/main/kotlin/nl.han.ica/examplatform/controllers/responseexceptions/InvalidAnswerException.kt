package nl.han.ica.examplatform.controllers.responseexceptions

class InvalidAnswerException(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
) : RuntimeException(message, cause, enableSuppression, writableStackTrace)