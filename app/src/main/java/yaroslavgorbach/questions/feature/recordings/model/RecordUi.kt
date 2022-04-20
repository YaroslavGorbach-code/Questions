package yaroslavgorbach.questions.feature.recordings.model

import android.text.format.DateUtils
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.ui.graphics.vector.ImageVector
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

data class RecordUi(
    val file: File,
    var recordState: RecordState = RecordState.Stop,
    var progress: Int = 0,
    var duration: Int = 0
) {

    sealed class RecordState {
        object Playing : RecordState()
        object Pause : RecordState()
        object Stop : RecordState()
    }

    val lastModified: String
        get() = DateUtils.getRelativeTimeSpanString(file.lastModified()).toString()

    val name: String
        get() = file.name

    val durationString: String
        get() = getFileDuration(file)

    val playIconRes: ImageVector
        get() = if (recordState == RecordState.Playing) Icons.Default.PauseCircle else Icons.Default.PlayCircle

    private fun getFileDuration(file: File): String {
        val duration = (file.absoluteFile.length() / 2f).toDouble()
        val durationL = duration.toLong()
        return String.format(
            Locale.US, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(durationL),
            TimeUnit.MILLISECONDS.toMinutes(durationL) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(durationL) % TimeUnit.MINUTES.toSeconds(1)
        )
    }
}