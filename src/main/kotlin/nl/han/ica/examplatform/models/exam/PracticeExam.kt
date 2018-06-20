package nl.han.ica.examplatform.models.exam

import io.swagger.annotations.ApiModelProperty
import nl.han.ica.examplatform.models.question.QuestionInPracticeExam

/**
 * Represents a exam for practise purposes
 */
data class PracticeExam(
        @ApiModelProperty(notes = "The name of the exam", required = true, allowEmptyValue = false)
        val name: String,
        @ApiModelProperty(notes = "The ID of the course that the exam is for", required = true)
        val courseId: Int,
        @ApiModelProperty(notes = "The questions in the exam")
        val questions: List<QuestionInPracticeExam>
)
