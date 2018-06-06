package nl.han.ica.examplatform.controllers.question

/**
 * Exception thrown when there is no [Question] available in the database for the given id.
 *
 * @property message [String] Message in exception
 * @property cause [Throwable] Previous exception
 */
class QuestionNotFoundException(
        message: String?,
        cause: Throwable? = null
) : RuntimeException(message, cause) {
    constructor() : this("The question that was requested is not available")
}