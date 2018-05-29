package nl.han.ica.examplatform.models.question

import io.swagger.annotations.ApiModelProperty
import java.util.Arrays

/**
 * Represents a question that can be asked in a [Exam].
 * A Question needs to have a [Answer] connected to it.
 *
 * @param questionId [Int] The id that the Question has in the database
 * @param questionOrderInExam [Int] The index number that represents the position of the question in the exam
 * @param questionOrderText [String] Text that go's along with the [questionOrderInExam]
 * @param questionType [QuestionType] The type of question this question is
 * @param questionText [String] The sentence that makes a request for information
 * @param questionPoints [Int] The amount of point the question is worth
 * @param subQuestions [Array]<[Question]> List of questions that represent one larger question
 */
data class Question(
    @ApiModelProperty(notes = "The ID of the question")
    val questionId: Int? = null,
    val questionOrderInExam: Int? = null,
    @ApiModelProperty(notes = "Text of order in exam text. Example: \"Question 1\" or \"a\"", required = true)
    val questionOrderText: String? = null,
    @ApiModelProperty(notes = "This could be open-, multiplechoice-, or noQuestion", required = true)
    val questionType: QuestionType = QuestionType.OPEN_QUESTION,
    @ApiModelProperty(notes = "Text of the question. This could be null if the question has subquestions")
    val questionText: String? = null,
    val questionPoints: Float,
    val options: Array<String>? = null,
    @ApiModelProperty(notes = "A question can contain subquestions when the type is noQuestion")
    val subQuestions: Array<Question>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question

        if (!Arrays.equals(subQuestions, other.subQuestions)) return false

        return true
    }

    override fun hashCode(): Int = subQuestions?.let { Arrays.hashCode(it) } ?: 0
}
