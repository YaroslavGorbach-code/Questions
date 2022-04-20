package yaroslavgorbach.questions.data.recordings

import android.content.Context
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoRecordsImp @Inject constructor(private val context: Context) : RepoRecords {

    override fun getRecordFiles(): List<File> {
        return (File(context.getExternalFilesDir("/")?.absolutePath ?: "")
            .listFiles() ?: emptyArray<File>()).toList()
    }

    override fun deleteRecordFile(file: File) {
        file.delete()
    }

    override fun deleteAllRecords() {
        getRecordFiles().forEach(this::deleteRecordFile)
    }
}