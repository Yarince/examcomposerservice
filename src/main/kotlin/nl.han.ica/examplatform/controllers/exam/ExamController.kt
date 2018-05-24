package nl.han.ica.examplatform.controllers.exam

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import nl.han.ica.examplatform.business.exam.ExamService
import nl.han.ica.examplatform.business.examquestion.ExamQuestionService
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.SimpleExam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("exams")
class ExamController {

    // Load examService as Spring Bean
    @Autowired
    lateinit var examService: ExamService

    @Autowired
    lateinit var examQuestionService: ExamQuestionService

    @PostMapping("/practice-exam")
    @ApiOperation(value = "Add an exam without questions", notes = "Cannot contain questions or an examId", response = ResponseEntity::class)
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request"))
    fun generatePracticeExam(@RequestBody courseId: Int): ResponseEntity<Exam?> = examService.generatePracticeExam(courseId)

    @GetMapping
    @ApiOperation(value = "Get a list of minified exams", notes = "This returns a list of exams containing ID, name, ", response = Array<SimpleExam>::class)
    fun getExams() = examService.getExams()

    @PostMapping()
    @ApiOperation(value = "Add an exam without questions", notes = "Cannot contain questions or an examId", response = ResponseEntity::class)
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request"))
    fun addExam(@RequestBody exam: Exam): ResponseEntity<Exam> = examService.addExam(exam)

    @GetMapping("/{id}")
    @ApiOperation(value = "Retrieve a specific exam", notes = "Retrieve a specific exam, containing all information, questions and answers", response = Exam::class)
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request"),
            ApiResponse(code = 404, message = "Not found"))
    fun getExam(@ApiParam(value = "The ID of the exam you want to retrieve", required = true) @PathVariable("id") id: Int): ResponseEntity<Exam> =
            examService.getExam(id)

    @PutMapping()
    @ApiOperation(value = "Add questions to a existing exam", notes = "Cannot contain questions or an examId", response = ResponseEntity::class)
    @ApiResponses(
            ApiResponse(code = 202, message = "Accepted"),
            ApiResponse(code = 403, message = "Bad request"))
    fun addQuestionToExam(@RequestBody exam: Exam): ResponseEntity<Exam> =
            examQuestionService.addQuestionToExam(exam)
}