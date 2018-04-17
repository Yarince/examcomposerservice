package nl.han.main.service.exam
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import nl.han.main.model.question.Question
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.reflect.KClass

@RestController
@Api("/api")
class ExamController {

    @RequestMapping("/exams", method = [RequestMethod.GET])
    @ApiOperation(value = "Find all tasks", notes = "Retrieving the collection of user tasks")
    @ApiResponses(
        ApiResponse(code = 200, message = "Success")
    )
    fun findAll() = "Placeholder"
            //Exam(1337, "Toets 1")

    @RequestMapping("/exams/{examId}", method = [RequestMethod.POST])
    fun findByLastName(@PathVariable examId: Int) = "Placeholder 2"
            //Exam(examId, "Toets 1", 10, Calendar.getInstance().time, null, "App", ExamType.EXAM, "Geen instructies", "b4.19")
}