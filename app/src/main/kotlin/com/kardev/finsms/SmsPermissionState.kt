package com.kardev.finsms

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat

fun hasSmsPermissions(context: Context): Boolean {
    val receive = ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS)
    val read = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS)
    return receive == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
}

@Composable
fun rememberSmsPermissionState(context: Context): Boolean {
    var granted by remember { mutableStateOf(hasSmsPermissions(context)) }
    return granted
}
