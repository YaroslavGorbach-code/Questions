package yaroslavgorbach.questions.feature.questions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import yaroslavgorbach.questions.bussines.questions.GetQuestionsInterractor
import yaroslavgorbach.questions.data.model.Question
import yaroslavgorbach.questions.feature.questions.model.QuestionsAction
import yaroslavgorbach.questions.feature.questions.model.QuestionsViewState
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getQuestionsInterractor: GetQuestionsInterractor,
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<QuestionsAction>()

    private val questions: MutableStateFlow<List<Question>> = MutableStateFlow(emptyList())

    private val isMenuExpended: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val state: StateFlow<QuestionsViewState> = combine(
        questions,
        isMenuExpended
    ) { questions, isMenuExpended ->
        QuestionsViewState(question = questions.first(), isMenuExpended = isMenuExpended)
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
}


