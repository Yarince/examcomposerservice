package nl.han.ica.examplatform.business.question

class QuestionNotInsertedException(
        message: String,
        cause: Throwable? = null
) : RuntimeException(message, cause)
