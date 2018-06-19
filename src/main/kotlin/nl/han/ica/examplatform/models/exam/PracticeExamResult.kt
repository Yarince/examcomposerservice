package nl.han.ica.examplatform.models.exam

import nl.han.ica.examplatform.models.question.QuestionResult

/**
 * Results of a practice exam.
 *
 * @param examId [Int] the ID of the exam that was made
 * @param studentNr [Int] the studentnumber of the student who made the exam
 * @param questions [ArrayList]<[QuestionResult]> the results of the questions made in this exam
 */
data class PracticeExamResult(
        val examId: Int,
        val studentNr: Int,
        val questions: ArrayList<QuestionResult>
)
