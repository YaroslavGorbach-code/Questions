package yaroslavgorbach.questions.feature.questions.model

import yaroslavgorbach.questions.data.questions.model.Question
import yaroslavgorbach.questions.utills.UiMessage

data class QuestionsViewState(
    val isMenuExpended: Boolean,
    val question: Question?,
    val isRecording: Boolean,
    val isRecordPermissionGranted: Boolean,
    val isPermissionWarningDialogVisible: Boolean,
    val message: UiMessage<QuestionsUiMessage>?
) {
    companion object {
        val Test = QuestionsViewState(
            question = Question.Test,
            isMenuExpended = false,
            isRecording = false,
            isRecordPermissionGranted = false,
            message = null,
            isPermissionWarningDialogVisible = false
        )
    }
}
