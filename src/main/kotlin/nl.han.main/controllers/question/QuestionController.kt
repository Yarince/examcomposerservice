package nl.han.main.controllers.question

import io.swagger.annotations.*
import nl.han.main.model.question.Question
import nl.han.main.service.question.QuestionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Api("question", description = "Creating, updating and deleting questions")
class QuestionController {

    @Autowired
    lateinit var questionService: QuestionService

    @RequestMapping("/question", method = [RequestMethod.POST])
    @ApiOperation(value = "Create a question", notes = "Create a question")
    @ApiResponses(
            ApiResponse(code = 201, message = "Created"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    fun createQuestion(@ApiParam(value = "Question object", required = true) @RequestBody question: Question) = questionService.addQuestion(question)
}