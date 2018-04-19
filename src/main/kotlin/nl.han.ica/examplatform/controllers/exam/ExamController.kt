package nl.han.ica.examplatform.controllers.exam

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.business.exam.ExamService
import nl.han.ica.examplatform.business.examquestion.ExamQuestionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("exam")
class ExamController {

    //Load examService as Spring Bean
    @Autowired
    lateinit var examService: ExamService

    @Autowired
    lateinit var examQuestionService: ExamQuestionService

    @GetMapping()
    @ApiOperation(value = "Example: Get all exams", notes = "Some extra information about this endpoint", response = Array<Exam>::class)
    fun getExams() =
            examService.getExams() // Example "get all" end-point

    @PostMapping()
    @ApiOperation(value = "Add an exam without questions", notes = "Cannot contain questions or an examId", response = ResponseEntity::class)
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request")
    )fun addExam(@RequestBody exam: Exam): ResponseEntity<Exam> =
            examService.addExam(exam)

    @PutMapping()
    @ApiOperation(value = "Add questions to a existing exam", notes = "Cannot contain questions or an examId", response = ResponseEntity::class)
    @ApiResponses(
            ApiResponse(code = 202, message = "Accepted"),
            ApiResponse(code = 403, message = "Bad request")
    )fun addQuestionToExam(@RequestBody exam: Exam): ResponseEntity<Exam> =
            examQuestionService.addQuestionToExam(exam)

}