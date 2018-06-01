package nl.han.ica.examplatform.persistence.category

import org.slf4j.Logger

/**
 * Interface for the category Database Access Object
 */
interface ICategoryDAO {
    val logger: Logger
    /**
     * Gets all categories within questions of a course.
     *
     * @param courseId [Int] The ID of course of which the questions should be retrieved.
     * @return [ArrayList]<[String]> An array of all categories corresponding to the course.
     */
    fun getCategoriesByCourse(courseId: Int): ArrayList<String>
}
