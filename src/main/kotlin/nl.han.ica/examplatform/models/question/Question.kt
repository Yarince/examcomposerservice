package nl.han.ica.examplatform.models.question

import io.swagger.annotations.ApiModelProperty
import nl.han.ica.examplatform.models.exam.ExamType


data class Question(
        @ApiModelProperty(notes = "The ID of the question")
        val questionId: Int? = null,

        @ApiModelProperty(notes = "Text of the question. This could be null if the question has subquestions")
        val questionText: String? = null,

        @ApiModelProperty(notes = "This could be open-, multiplechoice-, or noQuestion", required = true)
        val questionType: QuestionType = QuestionType.OPEN_QUESTION,

        @ApiModelProperty(notes = "The course that the question is for. For example: APP or SWA", required = true)
        val course: String = "N/S",

        @ApiModelProperty(notes = "SubID of a question, for example: a, b or c. If this question is not a subquestion this value should be null")
        val subId: String? = null,

        @ApiModelProperty(notes = "The type of the exam. This could be an EXAM, or a PRACTICE_EXAM", required = true)
        val examType: ExamType = ExamType.PRACTICE_EXAM,

        @ApiModelProperty(notes = "A question can contain subquestions when the type is noQuestion")
        val subQuestions: Array<Question>? = null
)