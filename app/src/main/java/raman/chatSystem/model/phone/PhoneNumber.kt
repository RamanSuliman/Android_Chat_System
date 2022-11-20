package raman.chatSystem.model.phone

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.O)
class PhoneNumber(private val context: Context, private val activity: Activity)
{
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNetworkCallback() = ActivityCompat.OnRequestPermissionsResultCallback { requestCode, permissions, grantResults ->
            when (requestCode) {
                121 -> {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        getPhoneNumber()
                    else
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    @SuppressLint("HardwareIds")
    @RequiresApi(Build.VERSION_CODES.O)
    fun getPhoneNumber() : String
    {
        createNetworkCallback()

        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_NUMBERS
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED)
        {
            activity.requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS), 121)
            return ""
        }
        return telephonyManager.line1Number
    }
}