package com.example.actionsrecord

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.actionsrecord.screens.HomeActivity
import com.example.actionsrecord.screens.SplashScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen(onSplashScreenFinished = { navigateToDashboardScreen() })
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun navigateToDashboardScreen() {
        setContent {
            HomeActivity()
        }
    }
}
