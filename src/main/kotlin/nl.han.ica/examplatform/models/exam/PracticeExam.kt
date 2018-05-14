package nl.han.ica.examplatform.models.exam

import io.swagger.annotations.ApiModelProperty
import nl.han.ica.examplatform.models.question.Question

data class PracticeExam(
        @ApiModelProperty(notes = "The name of the exam", required = true, allowEmptyValue = false)
        override val name: String,
        @ApiModelProperty(notes = "The ID of the course that the exam is for", required = true)
        override val courseId: Int,
        @ApiModelProperty(notes = "The version of the practice exam")
        override val version: Int? = 1,
        @ApiModelProperty(notes = "The type of the exam can be practice test or exam")
        override val examType: ExamType = ExamType.PRACTICE_EXAM,
        @ApiModelProperty(notes = "The questions in the exam")
        override val questions: ArrayList<Question>
): IExam