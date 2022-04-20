package yaroslavgorbach.questions.feature.recordings.model

import java.io.File

data class RecordsViewState(
    val records: List<RecordUi>
) {
    companion object {
        val Test = RecordsViewState(records = emptyList())
    }
}
