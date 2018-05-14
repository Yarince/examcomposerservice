package nl.han.ica.examplatform.models.exam

import io.swagger.annotations.ApiModelProperty
import nl.han.ica.examplatform.models.question.Question

data class PracticeExam(
        @ApiModelProperty(notes = "The name of the exam", required = true, allowEmptyValue = false)
        val name: String,
        @ApiModelProperty(notes = "The ID of the course that the exam is for", required = true)
        val courseId: Int,
        @ApiModelProperty(notes = "The version of the practice exam")
        val version: Int? = 1,
        @ApiModelProperty(notes = "The type of the exam can be practice test or exam")
        val examType: ExamType = ExamType.PRACTICE_EXAM,
        @ApiModelProperty(notes = "The questions in the exam")
        val questions: ArrayList<Question>
)