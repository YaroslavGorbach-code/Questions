package yaroslavgorbach.questions.feature.recordings.model

sealed class RecordsAction {
    class RecordCLick(val record: RecordUi) : RecordsAction()
    class DeleteRecord(val record: RecordUi) : RecordsAction()
    object DeleteAllRecords : RecordsAction()
}