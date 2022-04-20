package yaroslavgorbach.questions.feature.recordings.model

import java.io.File

sealed class RecordsAction {
    class PlayRecord(file: File) : RecordsAction()
    class DeleteRecord(file: File) : RecordsAction()
    object DeleteAllRecords : RecordsAction()
}