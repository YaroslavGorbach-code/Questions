package yaroslavgorbach.questions.data.recordings

import java.io.File

interface RepoRecords {

    fun getRecordFiles(): List<File>

    fun deleteRecordFile(file: File)

    fun deleteAllRecords()
}