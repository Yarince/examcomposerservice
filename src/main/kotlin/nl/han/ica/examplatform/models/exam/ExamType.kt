package nl.han.ica.examplatform.models.exam

/**
 * Represents the different types of [Exam]s that are available.
 *
 * @param examId [Int] The id of the ExamType in the database
 */
enum class ExamType(val examId: Int) {
    EXAM(1),
    PRACTICE_EXAM(2);

    companion object {
        /**
         * Finds the Enum type by the given id from the database.
         *
         * @param findValue [Int] The id of the ExamType in the database
         */
        fun from(findValue: Int): ExamType = ExamType.values().first { it.examId == findValue }
    }
}
