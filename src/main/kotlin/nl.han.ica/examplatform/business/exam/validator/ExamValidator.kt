package nl.han.ica.examplatform.business.exam.validator

import nl.han.ica.examplatform.exceptions.responseExceptions.InvalidExamException
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