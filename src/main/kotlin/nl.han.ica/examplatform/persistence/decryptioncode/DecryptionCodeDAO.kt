package nl.han.ica.examplatform.persistence.decryptioncode

import org.springframework.stereotype.Repository

/**
 * This class handles all the Database operations for decryption codes [String].
 */
@Repository
class DecryptionCodeDAO : IDecryptionCodeDAO {

    /**
     * This function gets a decryption code for a single exam from the database
     *
     * @return [String] decryption code
     */
    override fun getDecryptionCode(examId: Int): String = "Nice Code | ExamId: $examId"
}
