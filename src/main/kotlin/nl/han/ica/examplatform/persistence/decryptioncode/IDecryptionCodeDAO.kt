package nl.han.ica.examplatform.persistence.decryptioncode

interface IDecryptionCodeDAO {
    /**
     * This function gets a decryption code for a single exam from the database
     *
     * @return [String] decryption code
     */
    fun getDecryptionCode(examId: Int): String

    fun getAllDecryptionCodes(): ArrayList<Pair<String, String>>
}