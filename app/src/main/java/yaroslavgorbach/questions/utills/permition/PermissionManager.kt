package yaroslavgorbach.questions.utills.permition

import android.app.Activity
import androidx.activity.result.ActivityResultRegistry

interface PermissionManager {
    fun requestPermission(
        activityResultRegistry: ActivityResultRegistry?,
        permission: String,
        callback: (isGranted: Boolean) -> Unit,
    )

    fun isNeverAskAgainChecked(activity: Activity, permission: String): Boolean

    fun checkPermission(
        permission: String,
        callback: (isGranted: Boolean) -> Unit
    )

    fun checkPermission(
        permission: String,
    ): Boolean

    fun showSystemSettingsActivity(
        activityResultRegistry: ActivityResultRegistry,
    )

}