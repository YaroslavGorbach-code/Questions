package yaroslavgorbach.questions.utills.voicerecorder

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface VoiceRecorder {

    val isRecording: StateFlow<Boolean>

    val isRecordSaved: SharedFlow<Unit>

    suspend fun start(fileName: String)

    suspend fun stop()
}