package nl.han.ica.examplatform.controllers.responseExceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.NOT_FOUND)
class ExamNotFoundException(message: String) : RuntimeException(message)