package nl.han.ica.examplatform.controllers.question

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Thrown when there are categories present that dont exist in the database while inserting a question
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
class CategoriesDontExistException(message: String?) : RuntimeException(message)