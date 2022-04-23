package yaroslavgorbach.questions.feature.recordings.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import yaroslavgorbach.questions.bussines.recordings.DeleteRecordFileInteractor
import yaroslavgorbach.questions.bussines.recordings.GetRecordFilesInteractor
import yaroslavgorbach.questions.feature.recordings.model.RecordUi
import yaroslavgorbach.questions.feature.recordings.model.RecordsAction
import yaroslavgorbach.questions.feature.recordings.model.RecordsViewState
import yaroslavgorbach.questions.utills.player.RecordPlayer
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RecordingsViewModel @Inject constructor(
    private val getRecordFilesInteractor: GetRecordFilesInteractor,
    private val deleteRecordFileInteractor: DeleteRecordFileInteractor,
    private val recordPlayer: RecordPlayer
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<RecordsAction>()

    private val records: MutableStateFlow<List<RecordUi>> = MutableStateFlow(emptyList())

    val state: StateFlow<RecordsViewState> = combine(
        records,
        recordPlayer.progress,
        recordPlayer.duration,
    ) { records, progress, maxProgress ->
        RecordsViewState(records, progress, maxProgress)
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = RecordsViewState.Test
    )

    init {
        loadRecords()

        viewModelScope.launch {
            recordPlayer.finishEvent.collect {
                stoopAllPlaying()
            }
        }

        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    RecordsAction.DeleteAllRecords -> {

                    }

                    is RecordsAction.DeleteRecord -> {
                        deleteRecordFileInteractor.invoke(action.record.file)
                        records.update { records ->
                            records.toMutableList().apply {
                                remove(action.record)
                            }
                        }
                    }

                    is RecordsAction.RecordCLick -> {
                        records.update {
                            it.map { record ->
                                if (record == action.record) {
                                    when (record.recordState) {
                                        RecordUi.RecordState.Pause -> {
                                            recordPlayer.resume()
                                            record.copy(recordState = RecordUi.RecordState.Playing)
                                        }
                                        RecordUi.RecordState.Playing -> {
                                            recordPlayer.pause()
                                            record.copy(recordState = RecordUi.RecordState.Pause)
                                        }
                                        RecordUi.RecordState.Stop -> {
                                            stoopAllPlaying()
                                            recordPlayer.play(action.record.file)
                                            record.copy(recordState = RecordUi.RecordState.Playing)
                                        }
                                    }
                                } else {
                                    record
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun stoopAllPlaying() {
        recordPlayer.stop()
        records.update { rec ->
            rec.map { it.copy(recordState = RecordUi.RecordState.Stop) }
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






