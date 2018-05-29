package nl.han.ica.examplatform.business.course

import nl.han.ica.examplatform.models.course.Course
import nl.han.ica.examplatform.persistence.course.CourseDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class CourseService {

    @Autowired
    private lateinit var courseDAO: CourseDAO

    /**
     * This function gets a list of all courses
     *
     * @return [Array] of [Course]s
     */
    fun getAllCourses(): ResponseEntity<Array<Course>> = ResponseEntity(courseDAO.getAllCourses(), HttpStatus.OK)
}