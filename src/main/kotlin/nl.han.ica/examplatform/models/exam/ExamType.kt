package nl.han.ica.examplatform.models.exam

enum class ExamType(val examId: Int, val examCode: String) {
    EXAM(1, "EXAM"),
    PRACTICE_EXAM(2, "PRACTICE_EXAM")
}
