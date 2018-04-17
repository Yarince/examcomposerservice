package nl.han.ica.examplatform.controllers.exam

import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.service.exam.ExamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("exam")
class ExamController {

    //Load examService as Spring Bean
    @Autowired
    lateinit var examService: ExamService

    @GetMapping()
    fun getExams() =
            examService.getExams() // Example "get all" end-point

    @PostMapping()
    fun addExam(@RequestBody exam: Exam): ResponseEntity<Exam> =
            examService.addExam(exam)

}