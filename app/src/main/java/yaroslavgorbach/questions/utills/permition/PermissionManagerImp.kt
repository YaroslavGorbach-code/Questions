package yaroslavgorbach.questions.utills.permition

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale

class PermissionManagerImp(
    private val context: Context
) : PermissionManager {

    override fun requestPermission(
        activityResultRegistry: ActivityResultRegistry?,
        permission: String,
        callback: (isGranted: Boolean) -> Unit
    ) {
        activityResultRegistry?.register(
            permission,
            ActivityResultContracts.RequestPermission(),
            callback
        )?.launch(permission)
    }

    override fun isNeverAskAgainChecked(activity: Activity, permission: String): Boolean {
        return shouldShowRequestPermissionRationale(activity, permission).not()
    }

    override fun checkPermission(
        permission: String,
        callback: (isGranted: Boolean) -> Unit
    ) {
        callback(
            ActivityCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    override fun checkPermission(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun showSystemSettingsActivity(
        activityResultRegistry: ActivityResultRegistry,
    ) {
        activityResultRegistry.register(
            "settings",
            ActivityResultContracts.StartActivityForResult()
        ) {

        }.launch(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        })
    }

}
