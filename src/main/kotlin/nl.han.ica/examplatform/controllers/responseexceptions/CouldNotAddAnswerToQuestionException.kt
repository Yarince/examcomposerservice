package nl.han.ica.examplatform.controllers.responseexceptions

/**
 *
 */
class CouldNotAddAnswerToQuestionException(
    message: String?,
    cause: Throwable?,
    enableSuppression: Boolean,
    writableStackTrace: Boolean
) : RuntimeException(message, cause, enableSuppression, writableStackTrace)
