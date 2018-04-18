package nl.han.ica.examplatform.models.question

import io.swagger.annotations.ApiModelProperty

open class Question(
        @ApiModelProperty(notes = "The ID of the question")
        val questionId: Int,
        @ApiModelProperty(notes = "Text of the question")
        val questionText: String? = null,
        @ApiModelProperty(notes = "This could be open-, multiplechoice-, or noQuestion")
        val questionType: QuestionType? = null,
        @ApiModelProperty(notes = "The course that the question is for. For example: APP or SWA")
        val course: String = "test",
        @ApiModelProperty(notes = "SubID of a question, for example: a, b or c. If this question is not a subquestion this value should be null")
        val subId: String? = null,
        @ApiModelProperty(notes = "The type of the exam. This could be an EXAM, or a PRACTICE_EXAM")
        val examType: ExamType = ExamType.PRACTICE_EXAM,
        @ApiModelProperty(notes = "A question can contain subquestions when the type is noQuestion")
        val subQuestions: Array<Question>? = null
)