package nl.han.ica.examplatform.exceptions.answerExceptions

import nl.han.ica.examplatform.exceptions.ErrorInfo
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class CouldNotAddAnswerToQuestionException(errorInfo: ErrorInfo, cause: Throwable? = null) : RuntimeException(errorInfo.toString(), cause)
