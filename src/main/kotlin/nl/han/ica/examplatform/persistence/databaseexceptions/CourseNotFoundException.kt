package nl.han.ica.examplatform.persistence.databaseexceptions

import nl.han.ica.examplatform.models.course.Course
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Exception thrown when there is no [Course] found in the database
 * It is bound to a HTTP status [HttpStatus.NOT_FOUND]
 *
 * @param message [String] an additional message to further specify the error
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class CourseNotFoundException(message: String) : RuntimeException(message)
