package nl.han.ica.examplatform.models.exam

import io.swagger.annotations.ApiModelProperty
import nl.han.ica.examplatform.models.question.Question
import java.util.*

/**
 * Represents a exam for practise purposes
 */
data class PracticeExam(
        @ApiModelProperty(notes = "The name of the exam", required = true, allowEmptyValue = false)
        val name: String,
        @ApiModelProperty(notes = "The ID of the course that the exam is for", required = true)
        val courseId: Int,
        @ApiModelProperty(notes = "The questions in the exam")
        val questions: Array<Question>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PracticeExam

        if (name != other.name) return false
        if (courseId != other.courseId) return false
        if (!Arrays.equals(questions, other.questions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + courseId
        result = 31 * result + Arrays.hashCode(questions)
        return result
    }
}
