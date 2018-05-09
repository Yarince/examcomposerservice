package nl.han.ica.examplatform.controllers.exam

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import nl.han.ica.examplatform.business.exam.OfficialExamService
import nl.han.ica.examplatform.business.examquestion.ExamQuestionService
import nl.han.ica.examplatform.models.exam.OfficialExam
import nl.han.ica.examplatform.models.exam.SimpleExam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("exams")
class OfficialExamController {

    //Load officialExamService as Spring Bean
    @Autowired
    lateinit var officialExamService: OfficialExamService

    @Autowired
    lateinit var examQuestionService: ExamQuestionService

    @GetMapping
    @ApiOperation(value = "Get a list of minified exams", notes = "This returns a list of exams containing ID, name, ", response = Array<SimpleExam>::class)
    fun getExams() = officialExamService.getExams()

    @PostMapping()
    @ApiOperation(value = "Add an officialExam without questions", notes = "Cannot contain questions or an examId", response = ResponseEntity::class)
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request"))
    fun addExam(@RequestBody officialExam: OfficialExam): ResponseEntity<OfficialExam> = officialExamService.addExam(officialExam)

    @GetMapping("/{id}")
    @ApiOperation(value = "Retrieve a specific exam", notes = "Retrieve a specific exam, containing all information, questions and answers", response = OfficialExam::class)
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request"),
            ApiResponse(code = 404, message = "Not found"))
    fun getExam(@ApiParam(value = "The ID of the exam you want to retrieve", required = true) @PathVariable("id") id: Int): ResponseEntity<OfficialExam> =
            officialExamService.getExam(id)

    @PutMapping()
    @ApiOperation(value = "Add questions to a existing officialExam", notes = "Cannot contain questions or an examId", response = ResponseEntity::class)
    @ApiResponses(
            ApiResponse(code = 202, message = "Accepted"),
            ApiResponse(code = 403, message = "Bad request"))
    fun addQuestionToExam(@RequestBody officialExam: OfficialExam): ResponseEntity<OfficialExam> =
            examQuestionService.addQuestionToExam(officialExam)
}