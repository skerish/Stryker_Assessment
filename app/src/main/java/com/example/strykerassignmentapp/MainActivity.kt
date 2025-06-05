package com.example.strykerassignmentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.strykerassignmentapp.di.networkModule
import com.example.strykerassignmentapp.di.repositoryModule
import com.example.strykerassignmentapp.di.viewModelModule
import com.example.strykerassignmentapp.ui.navigation.Navigation
import com.example.strykerassignmentapp.ui.theme.StrykerAssignmentAppTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        startKoin{
            androidContext(this@MainActivity)
            modules(
                listOf(
                    networkModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }

        setContent {
            StrykerAssignmentAppTheme {
                Navigation()
            }
        }
    }
}