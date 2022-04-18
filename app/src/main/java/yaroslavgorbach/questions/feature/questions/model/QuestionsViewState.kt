package yaroslavgorbach.questions.feature.questions.model

import yaroslavgorbach.questions.data.model.Question


data class QuestionsViewState(
    val question: Question
) {
    companion object {
        val Test = QuestionsViewState(question = Question.Test)
    }
}
