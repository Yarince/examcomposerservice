package nl.han.ica.examplatform.models.question

import io.swagger.annotations.ApiModelProperty
import nl.han.ica.examplatform.models.exam.ExamType
import java.util.*


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
        val subQuestions: Array<Question>? = null,

        @ApiModelProperty(notes = "Only applicable if the question is a multipleChoice question. Array with strings containing the options")
        val options: Array<String>? = null,

        // todo: sequencenumber shouldnt be defaulted to 0, but database sequencenumber should be removed. not our fault but otherwise the tests don't work
        @ApiModelProperty(notes= "Number of the sequence in an exam if the question is in it")
        val sequenceNumber: Int? = 0
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