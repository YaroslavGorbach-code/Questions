package yaroslavgorbach.questions.feature.questions.model

sealed class QuestionsAction {
    object ExpendOrHideMenu : QuestionsAction()
    object ChangeQuestion : QuestionsAction()
    object LoadQuestions : QuestionsAction()
}