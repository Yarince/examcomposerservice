package nl.han.ica.examplatform.controllers.answer

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import nl.han.ica.examplatform.business.answer.AnswerService
import nl.han.ica.examplatform.controllers.responseexceptions.InvalidAnswerException
import nl.han.ica.examplatform.models.answer.OpenAnswer
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for interaction with answers
 */
@RestController
@RequestMapping("answers")
class AnswerController(private val answerService: AnswerService) {

    @PutMapping
    @ApiOperation(
            value = "Creates OpenAnswer and adds it to the question",
            notes = "If the question already has an correctQuestionAnswer it wil be overwritten",
            response = HttpStatus::class
    )
    @ApiResponses(
            ApiResponse(code = 200, message = "Answer created and added to question"),
            ApiResponse(code = 400, message = "Invalid correctQuestionAnswer"),
            ApiResponse(code = 500, message = "Something went wrong")
    )
    fun addOpenAnswerToQuestion(@RequestBody answer: OpenAnswer): HttpStatus {
        return try {
            answerService.addAnswerToQuestion(answer)
            HttpStatus.OK
        } catch (exception: IllegalArgumentException) {
            throw InvalidAnswerException("Answer contains invalid values", exception, false, false)
        }
    }

}
