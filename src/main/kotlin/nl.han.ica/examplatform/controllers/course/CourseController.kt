package nl.han.ica.examplatform.controllers.course

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import nl.han.ica.examplatform.business.course.CourseService
import nl.han.ica.examplatform.models.course.Course
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("courses")
class CourseController(private val courseService: CourseService) {

    @GetMapping("")
    @ApiOperation(value = "Get all courses", notes = "Returns all courses in the database", response = Array<Course>::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Fetched"),
            ApiResponse(code = 404, message = "No courses found"),
            ApiResponse(code = 500, message = "Internal server error"))
    fun getAllCourses(): ResponseEntity<Array<Course>> = courseService.getAllCourses()
}