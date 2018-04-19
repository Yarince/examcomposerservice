package nl.han.ica.examplatform.controllers.question

import io.swagger.annotations.*
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.business.question.QuestionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Api("question", description = "Creating, updating and deleting questions")
class QuestionController {

    @Autowired
    lateinit var questionService: QuestionService

    @PostMapping("/question")
    @ApiOperation(value = "Create a question", notes = "Create a question")
    @ApiResponses(
            ApiResponse(code = 201, message = "Created"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    fun createQuestion(@ApiParam(value = "Question object", required = true) @RequestBody question: Question): ResponseEntity<Question> = questionService.addQuestion(question)
}