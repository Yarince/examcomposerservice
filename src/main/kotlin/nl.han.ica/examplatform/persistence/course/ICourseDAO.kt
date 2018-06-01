package nl.han.ica.examplatform.persistence.course

import nl.han.ica.examplatform.models.course.Course

/**
 * This class handles all the Database operations for [Course]s
 */
interface ICourseDAO {
    /**
     * This function gets a list of all courses from the database.
     *
     * @return [ArrayList] of [Course]s
     */
    fun getAllCourses(): ArrayList<Course>
}