package nl.han.ica.examplatform.controllers.question

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import io.swagger.annotations.ApiParam
import nl.han.ica.examplatform.business.question.QuestionService
import nl.han.ica.examplatform.models.question.Question
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.GetMapping

@RestController
@Api("questions", description = "Creating, updating and deleting questions")
@RequestMapping("questions")
class QuestionController(val questionService: QuestionService) {

    /**
     * Endpoint for creating a question.
     *
     * @param question [Question] The question that should be inserted
     * @return [ResponseEntity]<[Question]> The inserted question
     */
    @PostMapping("/")
    @ApiOperation(value = "Create a question", notes = "Create a question")
    @ApiResponses(
            ApiResponse(code = 201, message = "Created"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    fun createQuestion(@ApiParam(value = "Question object", required = true) @RequestBody question: Question): ResponseEntity<Question> = questionService.addQuestion(question)

    /**
     * Endpoint for getting questions for a course.
     *
     * @param courseId [Int] The ID of the course of which the questions should be retrieved
     * @return [ResponseEntity]<[Array]<[Question]> The list of questions that corresponds to the course
     */
    @GetMapping("/{courseId}")
    @ApiOperation(value = "Retrieve questions of a course", notes = "Retrieve questions that are within a specific course", response = Array<Question>::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 403, message = "Bad request"),
            ApiResponse(code = 404, message = "Not found"))
    fun getQuestionsForCourse(@ApiParam(value = "The ID of the course you want to retrieve the questions of", required = true) @PathVariable("courseId") courseId: Int): ResponseEntity<Array<Question>> =
            questionService.getQuestionsForCourse(courseId)
}