package nl.han.ica.examplatform.business.course

import nl.han.ica.examplatform.models.course.Course
import nl.han.ica.examplatform.persistence.course.CourseDAO
import nl.han.ica.examplatform.persistence.course.ICourseDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

/**
 * Question service for handling requests related to the [Course] model.
 *
 * @param courseDAO [CourseDAO] The CourseDAO
 */
@Service
class CourseService(private val courseDAO: ICourseDAO) {

    /**
     * This function gets a list of all courses.
     *
     * @return [Array] of [Course]s
     */
    fun getAllCourses(): ResponseEntity<ArrayList<Course>> = ResponseEntity(courseDAO.getAllCourses(), HttpStatus.OK)
}
