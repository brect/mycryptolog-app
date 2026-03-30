package com.blimas.mycryptolog.application

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.blimas.mycryptolog.BuildConfig
import com.blimas.mycryptolog.presentation.navigation.NavGraph
import com.blimas.mycryptolog.presentation.theme.MyCryptoLogTheme
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val firebaseAppCheck = FirebaseAppCheck.getInstance()

        if (BuildConfig.DEBUG) {
            Log.d("MyAppCheck", "Installing App Check Debug Provider")
            firebaseAppCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance()
            )
        } else {
            Log.d("MyAppCheck", "Installing App Check Play Integrity Provider")
            firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance()
            )
        }
        
        enableEdgeToEdge()
        setContent {
            MyCryptoLogTheme {
                NavGraph()
            }
        }
    }
}