package org.jkc.event.tracker

import platform.Foundation.NSURL
import platform.UIKit.*
import platform.UIKit.UIApplication
import platform.UIKit.UIWindowScene
import platform.Foundation.*

actual fun shareUrl(url: String) {
    val nsUrl = NSURL.URLWithString(url) ?: return

    val activityViewController = UIActivityViewController(
        activityItems = listOf(nsUrl),
        applicationActivities = null
    )

    val controller = getRootViewController() ?: return
    controller.presentViewController(activityViewController, animated = true, completion = null)
}

private fun getRootViewController(): UIViewController? {
    val scenes = UIApplication.sharedApplication.connectedScenes
    val windowScene = scenes.firstOrNull() as? UIWindowScene
    val window = windowScene?.windows?.firstOrNull { it.isKeyWindow }
    return window?.rootViewController?.getTopViewController()
}

private fun UIViewController.getTopViewController(): UIViewController {
    return when (this) {
        is UINavigationController -> topViewController?.getTopViewController() ?: this
        is UITabBarController -> selectedViewController?.getTopViewController() ?: this
        else -> presentedViewController?.getTopViewController() ?: this
    }
}
