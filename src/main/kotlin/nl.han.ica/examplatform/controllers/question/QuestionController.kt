package nl.han.ica.examplatform.controllers.question

import io.swagger.annotations.*
import nl.han.ica.examplatform.business.question.QuestionService
import nl.han.ica.examplatform.business.question.QuestionTypeService
import nl.han.ica.examplatform.models.question.Question
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("/question")
@Api("question", description = "Creating, updating and deleting questions")
class QuestionController(private val questionService: QuestionService, private val questionTypeSerivce: QuestionTypeService) {

    @PostMapping()
    @ApiOperation(value = "Create a question", notes = "Create a question")
    @ApiResponses(
            ApiResponse(code = 201, message = "Created"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    fun createQuestion(@ApiParam(value = "Question object", required = true) @RequestBody question: Question): ResponseEntity<Question> = questionService.addQuestion(question)

    /**
     * HTTP REST function to add get questionTypes from the system.
     * @return All questionTypes in database
     */
    @GetMapping("/types")
    @ApiOperation(value = "Get all questionTypes", notes = "Get questionTypes")
    @ApiResponses(
            ApiResponse(code = 200, message = "Got questions"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    fun getQuestionTypes(): ResponseEntity<ArrayList<String>> = questionTypeSerivce.getQuestionTypes()
}