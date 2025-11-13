package com.example.truckingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.truckingtracker.appnavigation.AppNav
import com.example.truckingtracker.ui.theme.TruckingTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TruckingTrackerTheme {
                AppNav()

            }
        }
    }
}

