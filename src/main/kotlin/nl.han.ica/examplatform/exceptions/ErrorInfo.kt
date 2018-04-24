package nl.han.ica.examplatform.exceptions

/**
 * Representing the error send to the user of the API
 *
 * developerMessage : Short message about what went wrong inside the API for developing with the API
 * userMessage      : Short message (one sentence) to show to the actual user of the clients application
 * errorCode        : (Optional) Code which represents the error
 * moreInfo         : (Optional) Url to site which has more information about the error
 */
class ErrorInfo(
        private val developerMessage: String,
        private val userMessage: String,
        private val errorCode: String? = null,
        private val moreInfo: String? = null
) {
    override fun toString(): String {
        return """
            {
                developerMessage : $developerMessage,
                userMessage : $userMessage,
                errorCode : $errorCode,
                moreInfo : $moreInfo
            }
        """
    }
}

