package nl.han.ica.examplatform.controllers.responseexceptions

/**
 * Exception thrown when a [Answer] could not be added to a [Questtion].
 *
 * @param message [String] Message that discribes the origin of the exception
 * @param cause [Throwable] Previous exception
 */
class CouldNotAddAnswerToQuestionException(
    message: String?,
    cause: Throwable?
) : RuntimeException(message, cause)
