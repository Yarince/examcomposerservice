package nl.han.ica.examplatform.controllers.question

import io.swagger.annotations.*
import nl.han.ica.examplatform.business.question.QuestionService
import nl.han.ica.examplatform.business.question.QuestionTypeService
import nl.han.ica.examplatform.models.question.Question
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Api("question", description = "Creating, updating and deleting questions")
class QuestionController(private val questionService: QuestionService, private val questionTypeSerivce: QuestionTypeService) {

    @PostMapping("/question")
    @ApiOperation(value = "Create a question", notes = "Create a question")
    @ApiResponses(
            ApiResponse(code = 201, message = "Created"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    fun createQuestion(@ApiParam(value = "Question object", required = true) @RequestBody question: Question): ResponseEntity<Question> = questionService.addQuestion(question)

    @GetMapping("/types")
    @ApiOperation(value = "Get all questionTypes", notes = "Get questionTypes")
    @ApiResponses(
            ApiResponse(code = 200, message = "Got questions"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    fun getQuestionTypes(): ResponseEntity<ArrayList<String>> = questionTypeSerivce.getQuestionTypes()
}