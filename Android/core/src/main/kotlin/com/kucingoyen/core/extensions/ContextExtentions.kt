package com.kucingoyen.core.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

fun Context.copyToClipboard(label: String = "Copied", text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    if (clipboard == null) {
        return
    }
    try {
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
        Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(this, "Failed Copied", Toast.LENGTH_SHORT).show()
    }
}