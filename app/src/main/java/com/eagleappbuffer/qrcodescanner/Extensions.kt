package com.eagleappbuffer.qrcodescanner

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri

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
}
