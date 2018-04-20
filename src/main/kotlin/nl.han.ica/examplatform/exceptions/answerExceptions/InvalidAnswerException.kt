package nl.han.ica.examplatform.exceptions.answerExceptions

import nl.han.ica.examplatform.exceptions.ErrorInfo
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidAnswerException(errorInfo: ErrorInfo, cause: Throwable? = null) : RuntimeException(errorInfo.toString(), cause)