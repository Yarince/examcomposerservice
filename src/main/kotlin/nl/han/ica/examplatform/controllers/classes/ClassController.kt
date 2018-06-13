package nl.han.ica.examplatform.controllers.classes

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import nl.han.ica.examplatform.business.classes.ClassService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for HTTP interaction with Classes.
 *
 *@param classService [ClassService] The ClassService
 */
@RestController
@RequestMapping("class")
class ClassController(private val classService: ClassService) {

    /**
     * Returns all the available classes
     *
     * @return [ArrayList]<[String]> The ArrayList with all the retrieved classes
     */
    @GetMapping("/classes")
    @ApiOperation(
            value = "Returns all the available classes",
            response = HttpStatus::class
    )
    @ApiResponses(
            ApiResponse(code = 200, message = "Classes received"),
            ApiResponse(code = 400, message = "Invalid input"),
            ApiResponse(code = 500, message = "Something went wrong")
    )
    fun getAllClasses(): ResponseEntity<ArrayList<String>> = classService.getAllClasses()



}