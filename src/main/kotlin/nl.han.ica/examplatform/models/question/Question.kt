package nl.han.ica.examplatform.models.question

import io.swagger.annotations.ApiModelProperty
import java.util.Arrays


data class Question(
        @ApiModelProperty(notes = "The ID of the question")
        val questionId: Int? = null,
        val questionOrderInExam: Int? = null,
        @ApiModelProperty(notes = "Text of order in exam text. Example: \"Question 1\" or \"a\"", required = true)
        val questionOrderText: String? = null,
        @ApiModelProperty(notes = "This is the name of the plugin used, e.g. OpenQuestion", required = true)
        val questionType: String,
        @ApiModelProperty(notes = "Text of the question. This could be null if the question has subquestions")
        val questionText: String? = null,
        val questionPoints: Float? = null,
        @ApiModelProperty(notes = "ID of the course")
        val courseId: Int? = null,
        val options: Array<String>? = null,
        @ApiModelProperty(notes = "A question can contain subquestions when the type is noQuestion")
        val subQuestions: Array<Question>? = null,
        @ApiModelProperty(notes = "The categories that this question is about")
        var categories: Array<String> = arrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question

        if (!Arrays.equals(subQuestions, other.subQuestions)) return false

        return true
    }

    override fun hashCode(): Int {
        return subQuestions?.let { Arrays.hashCode(it) } ?: 0
    }
}