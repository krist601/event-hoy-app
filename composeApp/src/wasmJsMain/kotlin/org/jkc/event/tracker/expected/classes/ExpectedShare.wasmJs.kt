package org.jkc.event.tracker.expected.classes

import org.jkc.event.tracker.expected.interfaces.IExpectedShare

actual class ExpectedShare : IExpectedShare {
    actual override fun shareURL(url: String): Boolean {
        TODO("Not yet implemented")
    }
}