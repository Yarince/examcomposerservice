package nl.han.ica.examplatform.controllers.responseexceptions

/**
 * Exception thrown when there is an invalid [Answer] submitted.
 *
 * @property message [String] Message in exception
 * @property cause [Throwable] Previous exception
 */
class InvalidAnswerException(
    message: String?,
    cause: Throwable?
) : RuntimeException(message, cause)