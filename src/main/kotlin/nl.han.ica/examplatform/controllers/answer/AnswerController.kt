package nl.han.ica.examplatform.controllers.answer

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import nl.han.ica.examplatform.business.answer.AnswerService
import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.models.answermodel.answer.Answer
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for HTTP interaction with [Answer]s.
 */
@RestController
@RequestMapping("answers")
class AnswerController(private val answerService: AnswerService) {
    private val logger = loggerFor(javaClass)

    /**
     * HTTP REST function to add a [Answer] to a [Question]
     * If a Question already has an Answer connected to it,
     * the current Answer will be overwritten with the new Answer.
     *
     * @return [HttpStatus]
     */
    @PutMapping
    @ApiOperation(
            value = "Creates Answer and adds it to the question",
            notes = "If the question already has an answer it wil be overwritten",
            response = HttpStatus::class
    )
    @ApiResponses(
            ApiResponse(code = 200, message = "Answer created and added to question"),
            ApiResponse(code = 400, message = "Invalid Answer"),
            ApiResponse(code = 500, message = "Something went wrong")
    )
    fun addAnswerToQuestion(@RequestBody answer: Answer): HttpStatus =
            try {
                answerService.addAnswerToQuestion(answer)
                HttpStatus.OK
            } catch (exception: IllegalArgumentException) {
                logger.error("Tried to insert an answer with invalid values: $answer")
                throw InvalidAnswerException("Answer contains invalid values", exception)
            }
}
