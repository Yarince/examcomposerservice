package nl.han.ica.examplatform.models

/**
 * Representing the error send to the user of the API.
 *
 * @param developerMessage [String] Short message about what went wrong inside the API for developing with the API
 * @param userMessage [String] Short message (one sentence) to show to the actual user of the clients application
 * @param errorCode [String] Code which represents the error
 * @param moreInfo [String] Url to site which has more information about the error
 */
data class ErrorInfo(
    val developerMessage: String,
    val userMessage: String,
    val errorCode: String? = null,
    val moreInfo: String? = null
)
