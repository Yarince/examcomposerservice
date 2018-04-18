package nl.han.ica.examplatform.swagger

import io.swagger.annotations.*
import nl.han.ica.examplatform.models.question.Question
import org.springframework.web.bind.annotation.*

@RestController
@Api("test", description = "Description of this API endpoint group")
class ExampleController {

    @RequestMapping("/test", method = [RequestMethod.GET])
    // This represents the endpoint in the swagger UI
    @ApiOperation(value = "Find something", notes = "Some extra information about this endpoint", response = Question::class)
    // Specify the responses this endpoint can give. If you specify these they should be implemented as well.
    @ApiResponses(
            ApiResponse(code = 200, message = "Success"),
            ApiResponse(code = 403, message = "Forbidden"),
            ApiResponse(code = 401, message = "Unauthorized")
            // 500, 404 etc.
    )
    fun exampleEndpoint(@ApiParam(value = "Example json body parameter", required = true)
                        // The type of the request body specifies what kind of json should be sent in the body as param
                        @RequestBody requestBodyJson: Question) = "Replace this with the implementation method"

}