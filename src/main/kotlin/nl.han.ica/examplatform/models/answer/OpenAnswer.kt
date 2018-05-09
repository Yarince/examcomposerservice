package nl.han.ica.examplatform.models.answer

import io.swagger.annotations.ApiModelProperty

/**
 * Answer given by a teacher for a open question
 */
class OpenAnswer(
        @ApiModelProperty(notes = "The is of the answer")
        override val questionId: Int,

        @ApiModelProperty(notes = "The description of the answer")
        override val description: String,

        @ApiModelProperty(notes = "A comment on the answer")
        override val comment: String?,

        @ApiModelProperty(notes = "The keywords of the answer")
        private val keywords: Keywords
) : Answer