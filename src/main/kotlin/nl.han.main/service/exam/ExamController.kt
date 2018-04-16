package nl.han.main.service.exam

import nl.han.main.business.exam.model.Exam
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
    class ExamController {

    @GetMapping("/exams")
    fun findAll() =
            Exam(1337, "Toets 1")

    @GetMapping("/exams/{examId}")
    fun findByLastName(@PathVariable examId:Int) =
            Exam(examId, "Toets 1")
}