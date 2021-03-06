package nl.han.ica.examplatform.controllers.exam

import nl.han.ica.examplatform.models.exam.Exam

/**
 * Exception thrown when there is an error in [Exam].
 *
 * @property message [String] Message in exception
 * @property cause [Throwable] Previous exception
 */
class InvalidExamException(
        message: String,
        cause: Throwable? = null
) : RuntimeException(message, cause) {
    constructor() : this("The given exam was contained invalid properties.")
}
