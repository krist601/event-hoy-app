package org.jkc.event.tracker.expected.classes

import android.content.Context
import android.content.Intent
import org.jkc.event.tracker.expected.interfaces.IExpectedShare
import androidx.core.net.toUri

actual class ExpectedShare: IExpectedShare {

    private lateinit var appContext: Context

    fun initShareUtils(context: Context) {
        appContext = context.applicationContext
    }

    actual override fun shareURL(url: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK // esta línea es clave
            }
            appContext.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}