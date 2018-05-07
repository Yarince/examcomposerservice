package nl.han.ica.examplatform.controllers.responseexceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidExamException(message: String) : RuntimeException(message)