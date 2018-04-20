package nl.han.ica.examplatform.models.answer

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef

/**
 * Answer given by a teacher for a open question
 */
class OpenAnswer(
        override val questionId: Int,
        override val description: String,
        override val comment: String,
        private val keywords: Keywords
) : Answer {
    init {
        require(questionId > 0) { "Id has to be greater than zero" }
        require(description.isNotEmpty()) { "Description can not be empty" }
        require(comment.isNotEmpty()) { "Comment can not be empty" }
    }
}