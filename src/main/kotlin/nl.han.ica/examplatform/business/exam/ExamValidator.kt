package nl.han.ica.examplatform.business.exam

import nl.han.ica.examplatform.controllers.responseExceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam

class ExamValidator {
    companion object {
        fun validate(exam: Exam) {
            // Check if the examId is empty
            if (exam.examId != null)
                throw InvalidExamException("examId must be left empty")
        }
    }
}