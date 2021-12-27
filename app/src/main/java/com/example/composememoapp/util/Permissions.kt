package com.example.composememoapp.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Permissions {
    const val CAMERA = android.Manifest.permission.CAMERA
    const val READ_EXTERNAL_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE

    fun handlePermissionRequest(
        permission: String,
        context: Context,
        grantedAction: () -> Unit,
        requestPermissionAction: () -> Unit,
        rationaleAction: () -> Unit,
    ) {

        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            grantedAction()

        } else {

            if(ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)){
                Log.d("Permissions", "should : true")
                rationaleAction()
            }else{
                Log.d("Permissions", "should : false")
                requestPermissionAction()
            }
        }
    }
}