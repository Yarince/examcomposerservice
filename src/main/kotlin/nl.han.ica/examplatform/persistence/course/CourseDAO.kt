package nl.han.ica.examplatform.persistence.course

import nl.han.ica.examplatform.models.course.Course
import org.springframework.stereotype.Repository

@Repository
class CourseDAO {
    /**
     * This function gets a list of all courses from the database
     *
     * @return [Array] of [Course]s
     */
    fun getAllCourses(): Array<Course> {
        TODO("Not implemented")
    }
}