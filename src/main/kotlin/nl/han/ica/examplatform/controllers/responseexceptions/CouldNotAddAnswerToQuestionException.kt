package nl.han.ica.examplatform.controllers.responseexceptions

import nl.han.ica.examplatform.models.answermodel.answer.Answer
import nl.han.ica.examplatform.models.question.Question

/**
 * Exception thrown when a [Answer] could not be added to a [Question].
 *
 * @param message [String] Message that discribes the origin of the exception
 * @param cause [Throwable] Previous exception
 */
class CouldNotAddAnswerToQuestionException(
    message: String?,
    cause: Throwable?
) : RuntimeException(message, cause)
