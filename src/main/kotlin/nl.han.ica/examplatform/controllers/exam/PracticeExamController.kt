package nl.han.ica.examplatform.controllers.exam

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import nl.han.ica.examplatform.business.exam.PracticeExamService
import nl.han.ica.examplatform.business.examquestion.ExamQuestionService
import nl.han.ica.examplatform.models.exam.Exam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/practice-exams")
class PracticeExamController {

    @Autowired
    lateinit var practiceExamService: PracticeExamService

    @Autowired
    lateinit var examQuestionService: ExamQuestionService

    @PostMapping()
    @ApiOperation(value = "Add an exam without questions", notes = "Cannot contain questions or an examId", response = ResponseEntity::class)
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request"))
    fun addExam(@RequestBody exam: Exam): ResponseEntity<Exam> = practiceExamService.addExam(exam)

    @PostMapping()
    @ApiOperation(value = "Add an exam without questions", notes = "Cannot contain questions or an examId", response = ResponseEntity::class)
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request"))
    fun generatePracticeExam(): ResponseEntity<Exam> = practiceExamService.generatePracticeExam()
}