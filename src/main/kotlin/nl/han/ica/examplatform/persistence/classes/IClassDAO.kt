package nl.han.ica.examplatform.persistence.classes

interface IClassDAO {

    /**
     * Retrieves all the classes available
     *
     * @return [ArrayList]<[String]> The classes
     */
    fun getAllClasses(): ArrayList<String>
}