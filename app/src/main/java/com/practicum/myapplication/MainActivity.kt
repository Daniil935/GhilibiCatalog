package com.practicum.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.practicum.myapplication.di.AppContainer
import com.practicum.myapplication.navigation.AppNavigation
import com.practicum.myapplication.notification.ClassAlarmReceiver
import com.practicum.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val appContainer by lazy { AppContainer(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ClassAlarmReceiver.createNotificationChannel(this)

        setContent {
            MyApplicationTheme {
                AppNavigation(appContainer = appContainer)
            }
        }
    }
}
