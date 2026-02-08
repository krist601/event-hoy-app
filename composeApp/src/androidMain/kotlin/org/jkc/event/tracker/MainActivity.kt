package org.jkc.event.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.jkc.event.tracker.expected.classes.ExpectedShare
import org.jkc.event.tracker.expected.classes.LocationService
import org.jkc.event.tracker.expected.interfaces.IExpectedShare
import org.jkc.event.tracker.expected.interfaces.ILocationService
import org.koin.android.ext.koin.androidContext
import org.koin.mp.KoinPlatform.getKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val expectedShare: IExpectedShare = getKoin().get()
        (expectedShare as? ExpectedShare)?.initShareUtils(this)
        
        val locationService: ILocationService = getKoin().get()
        (locationService as? LocationService)?.initLocationService(this)

        setContent {
            App(
                {
                    androidContext(this@MainActivity.applicationContext)
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}