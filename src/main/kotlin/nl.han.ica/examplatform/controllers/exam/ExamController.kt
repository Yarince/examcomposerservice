package nl.han.ica.examplatform.controllers.exam

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import nl.han.ica.examplatform.business.exam.ExamService
import nl.han.ica.examplatform.business.examquestion.ExamQuestionService
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.PracticeExam
import nl.han.ica.examplatform.models.exam.PreparedExam
import nl.han.ica.examplatform.models.exam.PracticeExamRequestBody
import nl.han.ica.examplatform.models.exam.SimpleExam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("exams")
class ExamController {

    //Load examService as Spring Bean
    @Autowired
    lateinit var examService: ExamService

    @Autowired
    lateinit var examQuestionService: ExamQuestionService

    @PostMapping("/practice")
    @ApiOperation(value = "Generate a practice exam", notes = "This will return a random practice exam based on the course and the categories", response = PracticeExam::class)
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request"))
    fun generatePracticeExam(@RequestBody courseAndCategories: PracticeExamRequestBody): ResponseEntity<PracticeExam> = examService.generatePracticeExam(courseAndCategories.courseId, courseAndCategories.categories)

    @GetMapping
    @ApiOperation(value = "Get a list of minified exams", notes = "This returns a list of exams", response = Array<SimpleExam>::class)
    fun getExams() = examService.getExams()

    @PostMapping()
    @ApiOperation(value = "Add an exam without questions", notes = "Cannot contain questions or an examId", response = Exam::class)
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

    /**
     * Ads a class to an exam.
     *
     * @param examId [Int] the ID of the exam of which the classes should be added to.
     * @param classes [Array]<[String]> An array containing the classes.
     * @return [ResponseEntity]<[PreparedExam]> the exam containing the added classes.
     */
    @PostMapping("/addClasses")
    @ApiOperation(value = "Add classes to an exam", notes = "This makes it so the students are able to perform the exam", response = PreparedExam::class)
    @ApiResponses(
            ApiResponse(code = 202, message = "Accepted"),
            ApiResponse(code = 403, message = "Bad request"))
    fun addClassesToExam(
            @ApiParam(value = "An array of classes, e.g. ASD-A ASD-B ASD-C", required = true)
            @RequestParam classes: Array<String>,
            @ApiParam(value = "The ID of the exam you want to add the classes to", required = true)
            @RequestParam examId: Int) =
            examService.addClassesToExam(examId, classes)
}