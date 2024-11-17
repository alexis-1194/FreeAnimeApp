package com.example.jikanapp.test

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.jikanapp.R

class MainDelegateActivity : ComponentActivity(),
    AnalyticsLogger by AnalyticsLoggerImpl(),
    DeepLinkHandler by DeepLinkHandlerImpl() {

    private val obj by lazy {
        println("Hello World")
        3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLifeCycleOwner(this)

        println(obj)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(this, intent)
    }
}

interface DeepLinkHandler {
    fun handleDeepLink(activity: ComponentActivity, intent: Intent)
}

class DeepLinkHandlerImpl : DeepLinkHandler {
    override fun handleDeepLink(activity: ComponentActivity, intent: Intent) {

    }
}

interface AnalyticsLogger {
    fun registerLifeCycleOwner(owner: LifecycleOwner)
}

class AnalyticsLoggerImpl : AnalyticsLogger, LifecycleEventObserver {
    override fun registerLifeCycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> println("User Opened the screen")
            Lifecycle.Event.ON_PAUSE -> println("User Leaves the screen")
            else -> Unit
        }
    }
}