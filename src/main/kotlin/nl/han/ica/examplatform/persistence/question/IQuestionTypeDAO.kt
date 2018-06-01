package nl.han.ica.examplatform.persistence.question

/**
 * This class handles all the Database operations for question types.
 */
interface IQuestionTypeDAO {
    /**
     * This function gets a list of all questionTypes from the database.
     *
     * @return [ArrayList]<[String]> List of questionTypes
     */
    fun getAllQuestionTypes(): ArrayList<String>
}