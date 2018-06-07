package nl.han.ica.examplatform.business.answer

/**
 * Exception thrown when a [Answer] could not be added to a [Question].
 *
 * @param message [String] Message that discribes the origin of the exception
 * @param cause [Throwable] Previous exception
 */
class CouldNotAddAnswerToQuestionException(
        message: String?,
        cause: Throwable?
) : RuntimeException(message, cause)
