package nl.han.main.service.question

import io.swagger.annotations.*
import nl.han.main.model.question.Question
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Api("question", description = "Creating, updating and deleting questions")
class QuestionController {

    @RequestMapping("/question", method = [RequestMethod.POST])
    @ApiOperation(value = "Create a question", notes = "Create a question")
    @ApiResponses(
            ApiResponse(code = 200, message = "Success")
    )
    fun createQuestion(@ApiParam(value = "Question object", required = true) @RequestBody requestBodyJson: Question) = requestBodyJson
}