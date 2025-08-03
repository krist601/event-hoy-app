package org.jkc.event.tracker

/*import android.content.Intent
import android.content.Context

private lateinit var appContext: Context

fun initShareUtils(context: Context) {
    appContext = context.applicationContext
}

actual fun shareUrl(url: String) {
    if (!::appContext.isInitialized) return
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, url)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    appContext.startActivity(Intent.createChooser(intent, "Compartir enlace"))
}*/