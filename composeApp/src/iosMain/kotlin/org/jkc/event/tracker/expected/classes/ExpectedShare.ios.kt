package org.jkc.event.tracker.expected.classes

import org.jkc.event.tracker.bridges.interfaces.IShareBridge
import org.jkc.event.tracker.expected.interfaces.IExpectedShare

actual class ExpectedShare : IExpectedShare {
    private val platform: IShareBridge?
        get() = Companion.platformInstance

    fun setPlatform(platform: IShareBridge) {
        Companion.platformInstance = platform
    }

    companion object {
        var platformInstance: IShareBridge? = null
    }

    actual override fun shareURL(url: String): Boolean {
        return platform?.shareURL(url) ?: false
    }
}