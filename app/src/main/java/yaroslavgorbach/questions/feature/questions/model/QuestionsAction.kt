package yaroslavgorbach.questions.feature.questions.model

sealed class QuestionsAction {
    object ExpendOrHideMenu : QuestionsAction()
    object ChangeQuestion : QuestionsAction()
    object LoadQuestions : QuestionsAction()
    object StartRecording : QuestionsAction()
    object StopRecording : QuestionsAction()
    object ShowPermissionWarningDialog : QuestionsAction()
    object HidePermissionWarningDialog : QuestionsAction()
    object RequestAudioPermission : QuestionsAction()
    object GoToPermissionSettings : QuestionsAction()
}