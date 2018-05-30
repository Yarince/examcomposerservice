package nl.han.ica.examplatform.business.decryptioncode

import nl.han.ica.examplatform.persistence.decryptioncode.DecryptionCodeDAO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

/**
 * Decryption code service for retrieving the decryption code for a single exam.
 */
@Service
class DecryptionCodeService(private val decryptionCodeDAO: DecryptionCodeDAO) {

    /**
     * Returns decryption code [String] from the database
     *
     * @return [ResponseEntity]<String> decryption code [String]
     */
    fun getDecryptionCode(examId: Int): ResponseEntity<String> {
        val decryptionCode: String = decryptionCodeDAO.getDecryptionCode(examId)
        return ResponseEntity(decryptionCode, HttpStatus.OK)
    }
}
