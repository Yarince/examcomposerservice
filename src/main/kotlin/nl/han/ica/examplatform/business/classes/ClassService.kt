package nl.han.ica.examplatform.business.classes

import nl.han.ica.examplatform.persistence.classes.IClassDAO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


/**
 * Class service for handling requests related to the retrieval of Classes.
 *
 * @param classDAO [ClassDAO] The ClassDAO
 */
@Service
class ClassService(private val classDAO: IClassDAO) {

    /**
     * Retrieves all available classes
     *
     * @return [ArrayList]<[String]> The classes
     */
    fun getAllClasses() : ResponseEntity<ArrayList<String>> = ResponseEntity(classDAO.getAllClasses(), HttpStatus.OK)
}