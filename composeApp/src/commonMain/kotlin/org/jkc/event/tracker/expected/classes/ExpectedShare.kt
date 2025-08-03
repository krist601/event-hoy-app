package org.jkc.event.tracker.expected.classes

import org.jkc.event.tracker.expected.interfaces.IExpectedShare

expect class ExpectedShare(): IExpectedShare {
    override fun shareURL(url: String): Boolean
}