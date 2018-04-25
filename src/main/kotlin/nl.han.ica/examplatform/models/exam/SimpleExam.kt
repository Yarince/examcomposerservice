package nl.han.ica.examplatform.models.exam

// This DTO is needed for returning a list of all exams without details
data class SimpleExam(
        val examId: Int,
        val name: String,
        val course: String
)