package nl.han.ica.examplatform.controllers.responseexceptions

import nl.han.ica.examplatform.models.exam.Exam
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Exception thrown when there is an error in [Exam].
 * It is bound to a HTTP status [HttpStatus.BAD_REQUEST].
 *
 * @property message [String] Message in exception
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidExamException(message: String) : RuntimeException(message)