package yaroslavgorbach.questions.feature.questions.presentation

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import yaroslavgorbach.questions.bussines.questions.GetQuestionsInterractor
import yaroslavgorbach.questions.data.questions.model.Question
import yaroslavgorbach.questions.feature.questions.model.QuestionsAction
import yaroslavgorbach.questions.feature.questions.model.QuestionsUiMessage
import yaroslavgorbach.questions.feature.questions.model.QuestionsViewState
import yaroslavgorbach.questions.utills.UiMessage
import yaroslavgorbach.questions.utills.UiMessageManager
import yaroslavgorbach.questions.utills.permition.PermissionManager
import yaroslavgorbach.questions.utills.voicerecorder.VoiceRecorder
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getQuestionsInterractor: GetQuestionsInterractor,
    private val voiceRecorder: VoiceRecorder,
    private val permissionManager: PermissionManager
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<QuestionsAction>()

    private val questions: MutableStateFlow<List<Question>> = MutableStateFlow(emptyList())

    private val isMenuExpended: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val isRecording: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val uiMessageManager: UiMessageManager<QuestionsUiMessage> = UiMessageManager()

    private val isPermissionWarningDialogVisible: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    val state: StateFlow<QuestionsViewState> = combine(
        questions,
        isMenuExpended,
        isRecording,
        uiMessageManager.message,
        isPermissionWarningDialogVisible
    ) { questions, isMenuExpended, isRecording, message, isPermissionWarningDialogVisible ->
        QuestionsViewState(
            question = questions.firstOrNull(),
            isMenuExpended = isMenuExpended,
            isRecording = isRecording,
            isRecordPermissionGranted = permissionManager.checkPermission(Manifest.permission.RECORD_AUDIO),
            message = message,
            isPermissionWarningDialogVisible = isPermissionWarningDialogVisible
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = QuestionsViewState.Test
    )

    init {
        viewModelScope.launch {
            loadQuestions()
        }

        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    QuestionsAction.ExpendOrHideMenu -> {
                        isMenuExpended.update { it.not() }
                    }
                    QuestionsAction.ChangeQuestion -> {
                        questions.update {
                            it.drop(1)
                        }
                    }
                    QuestionsAction.LoadQuestions -> {
                        loadQuestions()
                    }
                    QuestionsAction.StartRecording -> {
                        voiceRecorder.start("")
                        isRecording.emit(true)
                    }
                    QuestionsAction.StopRecording -> {
                        if (isRecording.value) {
                            voiceRecorder.stop()
                            isRecording.emit(false)
                            uiMessageManager.emitMessage(UiMessage(QuestionsUiMessage.StopRecording))
                        }
                    }
                    QuestionsAction.RequestAudioPermission -> {
                        uiMessageManager.emitMessage(UiMessage(QuestionsUiMessage.RequestAudioPermission))
                    }
                    QuestionsAction.ShowPermissionWarningDialog -> {
                        isPermissionWarningDialogVisible.emit(true)
                    }
                    QuestionsAction.HidePermissionWarningDialog -> {
                        isPermissionWarningDialogVisible.emit(false)
                    }
                    QuestionsAction.GoToPermissionSettings -> {
                        uiMessageManager.emitMessage(UiMessage(QuestionsUiMessage.GoToPermissionSettings))
                    }
                }
            }
        }
    }

    private suspend fun loadQuestions() {
        getQuestionsInterractor.invoke()
            .flowOn(Dispatchers.IO)
            .collect(questions::emit)
    }

    fun submitAction(action: QuestionsAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}



