package nl.han.ica.examplatform.models.question

enum class QuestionType(val fieldName: String) {
    OPEN_QUESTION("MultipleChoice"),
    MULTIPLE_CHOICE_QUESTION("OpenQuestion"),
    NO_QUESTION("NoQuestion");

    companion object {
        fun from(findValue: String): QuestionType = QuestionType.values().first { it.fieldName == findValue }
    }
}
