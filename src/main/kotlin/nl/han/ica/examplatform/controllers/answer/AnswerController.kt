package nl.han.ica.examplatform.controllers.answer

import io.swagger.annotations.*
import nl.han.ica.examplatform.business.answer.AnswerService
import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.models.answermodel.AnswerModel
import nl.han.ica.examplatform.models.answermodel.answer.Answer
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.question.Question
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
            ApiResponse(code = 201, message = "Answer created and added to question"),
            ApiResponse(code = 400, message = "Invalid Answer"),
            ApiResponse(code = 500, message = "Something went wrong")
    )
    fun addAnswerToQuestion(@RequestBody answer: Answer): HttpStatus =
            try {
                answerService.addOrUpdateAnswerInQuestion(answer)
                HttpStatus.CREATED
            } catch (exception: IllegalArgumentException) {
                logger.error("Tried to insert an answer with invalid values: $answer")
                throw InvalidAnswerException("Answer contains invalid values", exception)
            }

    /**
     * HTTP REST function to add a [Answer] to a [Question] in [Exam]
     * If a Question already has an Answer connected to it,
     * the current Answer will be overwritten with the new Answer.
     *
     * @return [HttpStatus]
     */
    @PutMapping("/exam")
    @ApiOperation(
            value = "Creates Answer and adds it to the question in an exam",
            notes = "If the question already has an answer it wil be overwritten",
            response = HttpStatus::class
    )
    @ApiResponses(
            ApiResponse(code = 201, message = "Answer created and added to question in exam"),
            ApiResponse(code = 400, message = "Invalid Answer"),
            ApiResponse(code = 500, message = "Something went wrong")
    )
    fun addOrUpdateAnswerInQuestionInExam(
            @ApiParam(value = "The Answer you want to add to the exam", required = true)
            @RequestBody answer: Answer,
            @ApiParam(value = "The ID of the exam you want to add the answers to", required = true)
            @RequestParam examId: Int): HttpStatus =
            try {
                answerService.addOrUpdateAnswerInQuestionInExam(answer, examId)
                HttpStatus.CREATED
            } catch (ex: IllegalArgumentException) {
                logger.error("Tried to insert an answer with invalid values: $answer")
                throw InvalidAnswerException("Answer contains invalid values", ex)
            }

    /**
     * HTTP REST function to get a [Answer] to a [Question]
     *
     * @return [Answer] The answer of a question
     */
    @GetMapping
    @ApiOperation(
            value = "Gets an Answer for a question",
            response = Answer::class
    )
    @ApiResponses(
            ApiResponse(code = 200, message = "Answer fetched"),
            ApiResponse(code = 400, message = "Invalid Answer"),
            ApiResponse(code = 500, message = "Something went wrong")
    )
    fun getAnswerForQuestion(
            @ApiParam(value = "The ID of the questino you want to get the answer from", required = true)
            @RequestParam questionId: Int): ResponseEntity<Answer> =
            answerService.getAnswerForQuestion(questionId)

    /**
     * HTTP REST function to get a [Answer] to a [Exam]
     *
     * @return [AnswerModel] The answerModel of a Exam
     */
    @GetMapping("/exam")
    @ApiOperation(
            value = "Gets an Answer for a Exam",
            response = AnswerModel::class
    )
    @ApiResponses(
            ApiResponse(code = 200, message = "Answer fetched"),
            ApiResponse(code = 400, message = "Invalid Answer"),
            ApiResponse(code = 500, message = "Something went wrong")
    )
    fun getAnswersForExam(
            @ApiParam(value = "The ID of the questino you want to get the answer from", required = true)
            @RequestParam examId: Int): ResponseEntity<AnswerModel> =
            answerService.getAnswersForExam(examId)
}
