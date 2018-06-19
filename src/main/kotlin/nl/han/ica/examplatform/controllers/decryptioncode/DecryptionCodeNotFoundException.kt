package nl.han.ica.examplatform.controllers.decryptioncode

/**
 * Exception thrown when there is no [DecriptionCode] available in the database for the given exam.
 *
 * @property message [String] Message in exception
 * @property cause [Throwable] Previous exception
 */
class DecryptionCodeNotFoundException(
        message: String?,
        cause: Throwable? = null
) : RuntimeException(message, cause) {
    constructor() : this("""
                    |There is no Decryption Code in the Database for the given exam.
                    |Maybe the EFTS did not yet prepare this exam.
                    |""".trimMargin())
}
