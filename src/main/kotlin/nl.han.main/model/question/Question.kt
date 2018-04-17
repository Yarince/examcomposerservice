package nl.han.main.model.question

import io.swagger.annotations.ApiModelProperty

open class Question(
        @ApiModelProperty(notes = "The ID of the question")
        val questionId: Int,
        @ApiModelProperty(notes = "Text of the question")
        val questionText: String? = null,
        @ApiModelProperty(notes = "This could be open-, multiplechoice-, or noQuestion")
        val questionType: QuestionType? = null,
        @ApiModelProperty(notes = "A question can contain subquestions when the type is noQuestion")
        val subQuestions: Array<Question>? = null
)