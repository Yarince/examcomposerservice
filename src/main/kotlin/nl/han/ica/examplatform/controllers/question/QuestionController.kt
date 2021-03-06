package nl.han.ica.examplatform.controllers.question

import io.swagger.annotations.*
import nl.han.ica.examplatform.business.question.QuestionService
import nl.han.ica.examplatform.business.question.QuestionTypeService
import nl.han.ica.examplatform.models.question.Question
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for HTTP interaction with [Question]s.
 *
 * @param questionService [QuestionService] The QuestionService
 * @param questionTypeService [QuestionTypeService] The QuestionTypeService
 */
@RestController
@RequestMapping("question")
@Api("question", description = "Creating, updating and deleting questions")
class QuestionController(
        private val questionService: QuestionService,
        private val questionTypeService: QuestionTypeService
) {

    /**
     * HTTP REST function to add a new Question to the system.
     * Returns the newly added Question.
     *
     * @param question [Question] The question that should be inserted
     * @return [ResponseEntity]<[Question]> The inserted question
     */
    @PostMapping()
    @ApiOperation(value = "Create a question", notes = "Create a question. \n QuestionId and partialAnswer Ids should be null")
    @ApiResponses(
            ApiResponse(code = 201, message = "Created"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    fun createQuestion(
            @ApiParam(value = "Question object", required = true)
            @RequestBody question: Question
    ): ResponseEntity<Question> = questionService.addQuestion(question)

    /**
     * HTTP REST function to add a new sub Question to the system.
     * Returns the newly added Question.
     *
     * @param question [Question] The question that should be inserted
     * @return [ResponseEntity]<[Question]> The inserted question
     */
    @PostMapping("/{parentQuestionId}")
    @ApiOperation(value = "Create a sub question", notes = "Create a sub question.\n QuestionId and partialAnswer Ids should be null")
    @ApiResponses(
            ApiResponse(code = 201, message = "Created"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    fun createSubQuestion(
            @ApiParam(value = "The ID of the parent you want to create the sub question for.", required = true)
            @PathVariable("parentQuestionId") parentQuestionId: Int,
            @ApiParam(value = "Question object", required = true)
            @RequestBody question: Question
    ): ResponseEntity<Question> = questionService.addSubQuestion(question, parentQuestionId)

    /**
     * Endpoint for getting questions for a course.
     *
     * @param courseId [Int] The ID of the course of which the questions should be retrieved
     * @return [ResponseEntity]<[Array]<[Question]> The list of questions that corresponds to the course
     */
    @GetMapping("/course/{courseId}")
    @ApiOperation(
            value = "Retrieve questions of a course",
            notes = "Retrieve questions that are within a specific course",
            response = Array<Question>::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 403, message = "Bad request"),
            ApiResponse(code = 404, message = "Not found"))
    fun getQuestionsForCourse(
            @ApiParam(value = "The ID of the course you want to retrieve the questions of.", required = true)
            @PathVariable("courseId") courseId: Int
    ): ResponseEntity<Array<Question>> =
            questionService.getQuestionsForCourse(courseId)

    /**
     * Endpoint for getting a question by Id.
     *
     * @param questionId [Int] The ID of the question you want to retrieve.
     * @return [ResponseEntity]<[Question]> The question retrieved.
     */
    @GetMapping("/{questionId}")
    @ApiOperation(
            value = "Retrieve a Question by Id",
            notes = "Retrieve a question by id",
            response = Array<Question>::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 403, message = "Bad request"),
            ApiResponse(code = 404, message = "Not found"))
    fun getQuestionForId(
            @ApiParam(value = "The ID of the question you want to retrieve", required = true)
            @PathVariable("questionId") questionId: Int
    ): ResponseEntity<Question> =
            questionService.getQuestionForId(questionId)

    /**
     * HTTP REST function to get questionTypes from the system.
     * @return [String] of all questionTypes in database
     */
    @GetMapping("/types")
    @ApiOperation(value = "Get all questionTypes", notes = "Get questionTypes")
    @ApiResponses(
            ApiResponse(code = 200, message = "Got questions"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    fun getQuestionTypes(): ResponseEntity<ArrayList<String>> =
            questionTypeService.getQuestionTypes()

    /**
     * HTTP REST function to update a Question.
     * Returns the updated Question.
     *
     * @param question [Question] The question that should be updated
     * @return [ResponseEntity]<[Question]> The updated question
     */
    @PutMapping()
    @ApiOperation(value = "Update a question", notes = "Update a question")
    @ApiResponses(
            ApiResponse(code = 202, message = "Accepted"),
            ApiResponse(code = 404, message= "Not found"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    fun updateQuestion(
            @ApiParam(value = "Question object", required = true)
            @RequestBody question: Question): ResponseEntity<Question> =
            questionService.updateQuestion(question)
}
