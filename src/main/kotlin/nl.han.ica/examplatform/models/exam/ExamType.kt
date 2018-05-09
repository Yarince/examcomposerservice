package nl.han.ica.examplatform.models.exam

enum class ExamType(val examId: Int) {
    EXAM(1),
    PRACTICE_EXAM(2);

    companion object {
        fun from(findValue: Int): ExamType = ExamType.values().first { it.examId == findValue }
    }
}
