package yaroslavgorbach.questions.feature.questions.model

sealed class QuestionsUiMessage {
    object RequestAudioPermission : QuestionsUiMessage()
    object GoToPermissionSettings : QuestionsUiMessage()
    object StopRecording : QuestionsUiMessage()
}