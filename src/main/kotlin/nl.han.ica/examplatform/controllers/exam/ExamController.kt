package nl.han.ica.examplatform.controllers.exam

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.business.exam.ExamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("exam")
class ExamController {

    //Load examService as Spring Bean
    @Autowired
    lateinit var examService: ExamService

    @GetMapping("/all")
    @ApiOperation(value = "Example: Get all exams", notes = "Some extra information about this endpoint", response = Array<Exam>::class)
    fun getExams() =
            examService.getExams() // Example "get all" end-point

    @PostMapping()
    @ApiOperation(value = "Add an empty exam", notes = "Cannot contain questions or an examId", response = ResponseEntity::class)
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request")
    )
    fun addExam(@RequestBody exam: Exam): ResponseEntity<Exam> =
            examService.addExam(exam)

    @GetMapping()
    @ApiOperation(value = "Retrieve a specific exam", notes = "Retrieve a specific exam, containing all information, questions and answers", response = Exam::class)
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request"),
            ApiResponse(code = 404, message = "Not found")
    )
    fun getExam(@ApiParam(value = "The ID of the exam you want to retrieve", required = true) @RequestParam id: Int): ResponseEntity<Exam> =
            examService.getExam(id)
}