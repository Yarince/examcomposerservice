package nl.han.ica.examplatform.controllers.exam

import nl.han.ica.examplatform.models.exam.Exam
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Exception thrown when there is no [Exam] found when searching for one.
 *
 * @property message [String] an additional message to further specify the error
 * @property cause [Throwable] Previous exception
 */
class ExamNotFoundException(
        message: String,
        cause: Throwable? = null
) : RuntimeException(message, cause) {
    constructor() : this("The exam requested with the given id is not present in the database.")
}
