package nl.han.ica.examplatform.controllers.question

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import nl.han.ica.examplatform.business.question.QuestionService
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.question.Question
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for HTTP interaction with [Question]s.
 *
 * @param questionService [QuestionService] The QuestionService
 */
@RestController
@Api("question", description = "Creating, updating and deleting questions")
class QuestionController (private val questionService: QuestionService){

    /**
     * HTTP REST function to add a new Question to the system.
     * Returns the newly added Question.
     *
     * @return [Question]
     */
    @PostMapping("/question")
    @ApiOperation(value = "Create a question", notes = "Create a question")
    @ApiResponses(
            ApiResponse(code = 201, message = "Created"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    fun createQuestion(@ApiParam(value = "Question object", required = true) @RequestBody question: Question): ResponseEntity<Question> = questionService.addQuestion(question)
}