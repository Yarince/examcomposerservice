package nl.han.main.service.exam

import nl.han.main.model.exam.Exam
import nl.han.main.model.exam.ExamType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
    class ExamController {

    @GetMapping("/exams")
    fun findAll() =
            Exam(1337, "Toets 1")

    @GetMapping("/exams/{examId}")
    fun findByLastName(@PathVariable examId:Int) =
            Exam(examId, "Toets 1", 10, Calendar.getInstance().time, null, "App", ExamType.EXAM, "Geen instructies", "b4.19")
}