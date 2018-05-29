package nl.han.ica.examplatform.models

/**
 * Representing the error send to the user of the API.
 *
 * @param developerMessage : Short message about what went wrong inside the API for developing with the API
 * @param userMessage      : Short message (one sentence) to show to the actual user of the clients application
 * @param errorCode        : (Optional) Code which represents the error
 * @param moreInfo         : (Optional) Url to site which has more information about the error
 */
class ErrorInfo(
    val developerMessage: String,
    val userMessage: String,
    val errorCode: String? = null,
    val moreInfo: String? = null
)
