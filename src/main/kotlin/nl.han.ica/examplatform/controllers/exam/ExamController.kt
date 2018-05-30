package nl.han.ica.examplatform.controllers.exam

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import nl.han.ica.examplatform.business.exam.ExamService
import nl.han.ica.examplatform.business.examquestion.ExamQuestionService
import nl.han.ica.examplatform.models.exam.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for HTTP interaction with [Exam]s.
 *
 * @param examService [ExamService] The ExamService
 * @param examQuestionService [examQuestionService] The ExamQuestionService
 */
@RestController
@RequestMapping("exams")
class ExamController (
    private val examService: ExamService,
    private val examQuestionService: ExamQuestionService
) {

    /**
     * HTTP REST function to generate a practice exam.
     *
     * @return [PracticeExam]
     */
    @PostMapping("/practice-exam")
    @ApiOperation(
        value = "Add a practice exam without questions",
        notes = "Cannot contain questions or an examId",
        response = ResponseEntity::class
    )
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request"))
    fun generatePracticeExam(@RequestBody courseAndCategories: PracticeExamRequestBody): ResponseEntity<PracticeExam> = examService.generatePracticeExam(courseAndCategories.courseId, courseAndCategories.categories)


    /**
     * HTTP REST function to get a list of basic information from all exams.
     *
     * @return [Array]<[SimpleExam]>
     */
    @GetMapping
    @ApiOperation(
        value = "Get a list of minified exams",
        notes = "This returns a list of exams containing ID, name, ",
        response = Array<SimpleExam>::class
    )
    fun getExams() = examService.getExams()

    /**
     * HTTP REST function add a new Exam to the system.
     * Returns the newly added Exam.
     *
     * @return [Exam]
     */
    @PostMapping()
    @ApiOperation(
        value = "Add an exam without questions",
        notes = "Cannot contain questions or an examId",
        response = ResponseEntity::class
    )
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request"))
    fun addExam(@RequestBody exam: Exam): ResponseEntity<Exam> = examService.addExam(exam)

    /**
     * HTTP REST function to get one exam with all details.
     *
     * @return [Exam]
     */
    @GetMapping("/{id}")
    @ApiOperation(
        value = "Retrieve a specific exam",
        notes = "Retrieve a specific exam, containing all information, questions and answers",
        response = Exam::class
    )
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request"),
            ApiResponse(code = 404, message = "Not found"))
    fun getExam(
        @ApiParam(value = "The ID of the exam you want to retrieve", required = true)
        @PathVariable("id")
        id: Int
    ): ResponseEntity<Exam> = examService.getExam(id)

    /**
     * HTTP REST function to add one [Question] to a Exam.
     * Returns the given Exam with the newly added Question.
     *
     * @return [Exam]
     */
    @PutMapping()
    @ApiOperation(
        value = "Add questions to a existing exam", notes = "Cannot contain questions or an examId",
        response = ResponseEntity::class
    )
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
