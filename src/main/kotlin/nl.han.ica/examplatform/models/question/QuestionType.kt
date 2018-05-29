package nl.han.ica.examplatform.models.question

/**
 * Represents the different types of [Question]s that are available.
 *
 * @param fieldName [String] the name of the QuestionType in the database.
 */
enum class QuestionType(val fieldName: String) {
    OPEN_QUESTION("MultipleChoice"),
    MULTIPLE_CHOICE_QUESTION("OpenQuestion"),
    NO_QUESTION("NoQuestion");

    companion object {
        /**
         * Finds the Enum type by the given id from the database.
         *
         * @param findValue [String] The name of the QuestionType in the database
         */
        fun from(findValue: String): QuestionType = QuestionType.values().first { it.fieldName == findValue }
    }
}
