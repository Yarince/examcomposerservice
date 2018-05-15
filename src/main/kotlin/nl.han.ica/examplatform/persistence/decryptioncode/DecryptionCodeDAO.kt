package nl.han.ica.examplatform.persistence.decryptioncode

import org.springframework.stereotype.Repository

@Repository
class DecryptionCodeDAO {
    fun getDecryptionCode(examId: Int): String {
        return "Nice Code"
    }
}