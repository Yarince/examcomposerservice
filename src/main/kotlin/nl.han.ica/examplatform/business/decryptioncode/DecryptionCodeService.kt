package nl.han.ica.examplatform.business.decryptioncode

import nl.han.ica.examplatform.persistence.decryptioncode.DecryptionCodeDAO
import org.springframework.stereotype.Service

@Service
class DecryptionCodeService(private val decryptionCodeDAO: DecryptionCodeDAO) {

    fun getDecryptionCode(examId: Int): String {
        return decryptionCodeDAO.getDecryptionCode(examId)
    }
}