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

    /**
     * Adds categories to a question.
     *
     * @param categories [ArrayList]<[String]> The categories to add
     * @param questionId [Int] The ID of the question of which the categories should be added to
     */
    fun addCategoriesToQuestion(categories: ArrayList<String>, questionId: Int)

    fun checkIfCategoriesExist(categories: ArrayList<String>): Boolean
}
