package yaroslavgorbach.questions.utills.player

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface RecordPlayer {
    val progress: StateFlow<Int>
    val duration: StateFlow<Int>
    val finishEvent: SharedFlow<Unit>

    suspend fun play(file: File)
    suspend fun stop()
    suspend fun pause()
    suspend fun resume()
    suspend fun seekToo(progress: Int)
}