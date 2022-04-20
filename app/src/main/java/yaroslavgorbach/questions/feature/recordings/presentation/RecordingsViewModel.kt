package yaroslavgorbach.questions.feature.recordings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import yaroslavgorbach.questions.bussines.recordings.GetRecordFilesInteractor
import yaroslavgorbach.questions.feature.recordings.model.RecordUi
import yaroslavgorbach.questions.feature.recordings.model.RecordsAction
import yaroslavgorbach.questions.feature.recordings.model.RecordsViewState
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RecordingsViewModel @Inject constructor(
    private val getRecordFilesInteractor: GetRecordFilesInteractor
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<RecordsAction>()

    private val records: MutableStateFlow<List<RecordUi>> = MutableStateFlow(emptyList())

    val state: StateFlow<RecordsViewState> = combine(
        records
    ) { records ->
        RecordsViewState(records.first())
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = RecordsViewState.Test
    )

    init {
        loadRecords()

        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    RecordsAction.DeleteAllRecords -> {

                    }
                    is RecordsAction.DeleteRecord -> {

                    }
                    is RecordsAction.PlayRecord -> {

                    }
                }
            }
        }
    }

    private fun loadRecords() {
        getRecordFilesInteractor()
            .sortedByDescending(File::lastModified)
            .map(::RecordUi)
            .also {
                viewModelScope.launch {
                    records.emit(it)
                }
            }
    }

    fun submitAction(action: RecordsAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}






