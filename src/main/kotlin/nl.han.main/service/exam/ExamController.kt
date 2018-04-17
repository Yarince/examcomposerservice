package nl.han.main.service.exam
import org.springframework.web.bind.annotation.*

@RestController
class ExamController {

    @RequestMapping("/exams", method = [RequestMethod.GET])
    fun findAll() = "Placeholder"

    @RequestMapping("/exams/{examId}", method = [RequestMethod.POST])
    fun findByLastName(@PathVariable examId: Int) = "Placeholder 2"
}