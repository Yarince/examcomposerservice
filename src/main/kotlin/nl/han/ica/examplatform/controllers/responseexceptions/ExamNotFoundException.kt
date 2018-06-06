package nl.han.ica.examplatform.controllers.responseexceptions

import nl.han.ica.examplatform.models.exam.Exam
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Exception thrown when there is no [Exam] found when searching for one.
 * It is bound to a HTTP status [HttpStatus.NOT_FOUND].
 *
 * @param message [String] an additional message to further specify the error
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class ExamNotFoundException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CategoriesDontExist(message: String?) : RuntimeException(message)