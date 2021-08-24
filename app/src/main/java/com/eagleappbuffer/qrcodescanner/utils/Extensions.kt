package com.eagleappbuffer.qrcodescanner.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun Activity.showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Activity.shareCode(value: String) {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.putExtra(
        Intent.EXTRA_TEXT,
        value
    )
    intent.type = "text/plain"
    val shareIntent = Intent.createChooser(intent, null)
    startActivity(shareIntent)
}

fun Activity.openBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(Intent.createChooser(intent, "Choose Your browser"))
}

fun Activity.copyValue(value: String) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData: ClipData = ClipData.newPlainText("value", value)
    clipboardManager.setPrimaryClip(clipData)
    showToast("Copied")
}

fun Activity.showAlert(msg: String /*dialogInterface: DialogInterface.OnClickListener*/) {
    val pickDialog = android.app.AlertDialog.Builder(this)
    pickDialog.setCancelable(false)
//    pickDialog.setPositiveButton("OK", dialogInterface)
    pickDialog.setPositiveButton(
        "OK"
    ) { dialog, which -> dialog.dismiss() }
    pickDialog.setMessage(msg)
    pickDialog.show()
}