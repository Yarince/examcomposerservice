package nl.han.ica.examplatform.persistence.course

import nl.han.ica.examplatform.models.course.Course
import org.springframework.stereotype.Repository

@Repository
class CourseDAO {
    /**
     * This function gets a list of all courses from the database
     *
     * @return [ArrayList] of [Course]s
     */
    fun getAllCourses(): ArrayList<Course> {
        return arrayListOf(Course(1, "A course with a name", "acwan"),
                Course(2, "Software Architecture", "swa"))
    }
}