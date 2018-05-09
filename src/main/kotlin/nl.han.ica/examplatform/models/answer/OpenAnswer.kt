package nl.han.ica.examplatform.models.answer

import io.swagger.annotations.ApiModelProperty

/**
 * Answer given by a teacher for a open question
 */
class OpenAnswer(
        @ApiModelProperty(notes = "The is of the correctQuestionAnswer")
        override val questionId: Int,

        @ApiModelProperty(notes = "The description of the correctQuestionAnswer")
        override val correctQuestionAnswer: String,

        @ApiModelProperty(notes = "The keywords of the correctQuestionAnswer")
        override val answerKeywords: Keywords?
) : Answer