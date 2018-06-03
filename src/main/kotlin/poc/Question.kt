package poc

import java.util.*

internal data class Question(val questionId: Int, val questionText: String, val categories: Array<String>, val type: String, val answeredOn: Date? = null)
