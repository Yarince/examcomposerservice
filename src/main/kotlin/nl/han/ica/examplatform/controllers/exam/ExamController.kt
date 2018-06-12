package nl.han.ica.examplatform.controllers.exam

import io.swagger.annotations.*
import nl.han.ica.examplatform.business.exam.ExamService
import nl.han.ica.examplatform.business.examquestion.ExamQuestionService
import nl.han.ica.examplatform.models.exam.*
import nl.han.ica.examplatform.models.question.Question
import org.springframework.http.HttpStatus
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
class ExamController(
        private val examService: ExamService,
        private val examQuestionService: ExamQuestionService
) {

    /**
     * HTTP REST function to generate a practice exam.
     *
     * @return [PracticeExam]
     */
    @GetMapping("/practice-exam")
    @ApiOperation(
            value = "Add a practice exam without questions",
            notes = "Cannot contain questions or an examId",
            response = PracticeExam::class
    )
    @ApiResponses(
            ApiResponse(code = 201, message = "Create"),
            ApiResponse(code = 403, message = "Bad request"))
    fun generatePracticeExam(
            @RequestParam courseId: Int,
            @RequestParam studentNr: Int): ResponseEntity<PracticeExam> = examService.generatePracticeExam(courseId, studentNr)


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
    fun getExams(): ResponseEntity<ArrayList<SimpleExam>> = examService.getExams()

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
            response = Exam::class
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
    @PutMapping("/addQuestions")
    @ApiOperation(
            value = "Add questions to a existing exam", notes = "Cannot contain questions or an examId",
            response = Exam::class
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
     * @return [ResponseEntity]<[Exam]> the exam containing the added classes.
     */
    @PostMapping("/addClasses")
    @ApiOperation(
            value = "Add classes to an exam",
            notes = "This makes it so the students are able to perform the exam",
            response = Exam::class)
    @ApiResponses(
            ApiResponse(code = 202, message = "Accepted"),
            ApiResponse(code = 403, message = "Bad request"))
    fun addClassesToExam(
            @ApiParam(value = "An array of classes, e.g. ASD-A ASD-B ASD-C", required = true)
            @RequestParam classes: ArrayList<String>,
            @ApiParam(value = "The ID of the exam you want to add the classes to", required = true)
            @RequestParam examId: Int
    ): ResponseEntity<HttpStatus> =
            examService.addClassesToExam(examId, classes)

    /**
     * HTTP REST function to publish an Exam.
     * This makes the exam ready for download.
     *
     * @param examId [Int] the ID of the exam that should be published
     */
    @PutMapping("/publish")
    @ApiOperation(
            value = "Publish an exam", notes = "This makes the exam ready for download for the students",
            response = PreparedExam::class
    )
    @ApiResponses(
            ApiResponse(code = 202, message = "Accepted"),
            ApiResponse(code = 403, message = "Bad request"))
    fun publishExam(
            @ApiParam(value = "The ID of the exam", required = true)
            @RequestParam examId: Int,
            @ApiParam(value = "If the exam should be published, or un-published. Defaults to published (true)", required = false)
            @RequestParam shouldBePublished: Boolean = true) = examService.publishExam(examId, shouldBePublished)

    /**
     * HTTP REST function to change the order of questions in an exam.
     *
     * @param examId [Int] the ID of the exam of which the order of questions should be changed
     */
    @PutMapping("/changeOrder")
    @ApiOperation(
            value = "Changes the order of questions in an exam"
    )
    @ApiResponses(
            ApiResponse(code = 202, message = "Accepted"),
            ApiResponse(code = 403, message = "Bad request"))
    fun changeQuestionOrderInExam(
            @ApiParam(value = "The ID of the exam", required = true)
            @RequestParam examId: Int,
            @ApiParam(value = "This is an array of pairs. The pairs first value is the question ID, " +
                    "the second value is the new sequence number", required = true)
            @RequestBody questionsAndSequenceNumbers: Array<Pair<Int, Int>>) =
            examQuestionService.changeQuestionOrderInExam(examId, questionsAndSequenceNumbers)

    /**
     * HTTP REST function to update an [Exam].
     * This does not include the questions in an exam.
     *
     * @param exam [Exam] The updated exam
     * @return [Exam] The result of the update
     */
    @PutMapping()
    @ApiOperation(
            value = "Update meta data in an exam", notes = "This does not include updating of questions in this exam",
            response = Exam::class
    )
    @ApiResponses(
            ApiResponse(code = 202, message = "Accepted"),
            ApiResponse(code = 403, message = "Bad request"))
    fun updateExam(@RequestBody exam: Exam): ResponseEntity<Exam> =
            examService.updateExam(exam)

    /**
     * HTTP REST function to delete an [Exam].
     * This does not delete the questions in an exam.
     *
     * @param examId [Int] The ID of the exam to delete
     */
    @DeleteMapping()
    @ApiOperation(
            value = "Delete an exam",
            notes = "This only deletes the exam information, not the questions"
    )
    fun deleteExam(@RequestParam examId: Int) = examService.deleteExam(examId)

    /**
     * HTTP REST function that de-couples questions from an exam.
     *
     * @param examId [Int] The ID of the exam
     * @param questionIds [Array]<[Int]> Array containing the IDs of the questions that should be removed
     */
    @PutMapping("/removeQuestions")
    @ApiOperation(
            value = "Removes questions from an exam",
            notes = "This doesn't remove any questions, but just decouples them from the exam"
    )
    @ApiResponses(
            ApiResponse(code = 202, message = "Accepted"),
            ApiResponse(code = 403, message = "Bad request"))
    fun removeQuestionFromExam(
            @ApiParam(value = "The ID of the exam", required = true)
            @RequestParam examId: Int,
            @ApiParam(value = "An array containing the questionIds that should be removed from the exam. Put every item on a newline in swagger", required = true)
            @RequestParam questionIds: Array<Int>) = examQuestionService.removeQuestionsFromExam(examId, questionIds)

    /**
     * Returns the decryption code to unlock the exam
     *
     * @return [String]
     */
    @GetMapping("/classes")
    @ApiOperation(
            value = "Returns the decryption code to unlock the exam",
            response = HttpStatus::class
    )
    @ApiResponses(
            ApiResponse(code = 200, message = "Decryption code received"),
            ApiResponse(code = 400, message = "Invalid Answer"),
            ApiResponse(code = 500, message = "Something went wrong")
    )
    fun getAllClasses(): ResponseEntity<ArrayList<String>> = examService.getAllClasses()


}
