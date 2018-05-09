package nl.han.ica.examplatform.models.question

import io.swagger.annotations.ApiModelProperty
import nl.han.ica.examplatform.models.exam.ExamType
import java.util.*


data class Question(
        @ApiModelProperty(notes = "The ID of the question")
        val questionId: Int? = null,

        @ApiModelProperty(notes = "The parent id of the question")
        val parentQuestionId: Int? = null,

        @ApiModelProperty(notes = "The type of the exam. This could be an EXAM, or a PRACTICE_EXAM", required = true)
        val examTypeId: ExamType = ExamType.PRACTICE_EXAM,

        @ApiModelProperty(notes = "The course that the question is for. For example: APP or SWA", required = true)
        val courseId: Int,

        @ApiModelProperty(notes = "Text of the question. This could be null if the question has subquestions")
        val questionText: String? = null,

        @ApiModelProperty(notes = "This could be open-, multiplechoice-, or noQuestion", required = true)
        val questionType: QuestionType = QuestionType.OPEN_QUESTION,

        @ApiModelProperty(notes = "The answer of the question")
        val answerText: String? = null,

        @ApiModelProperty(notes = "The answer keywords of a question")
        val answerKeywords: String? = null,

        @ApiModelProperty(notes = "The assessment comments of a teacher for the question in an exam")
        val assessmentComments: String? = null,

        // Have to figure out how we deal with these 2 variables. If they get removed they will break a whole lot.
        @ApiModelProperty(notes = "A question can contain subquestions when the type is noQuestion")
        val subQuestions: Array<Question>? = null,

        @ApiModelProperty(notes = "Only applicable if the question is a multipleChoice question. Array with strings containing the options")
        val options: Array<String>? = null,

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