package com.example.shopna

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.navigator.Navigator
import com.example.shopna.data.network.RetrofitInstance
import com.example.shopna.presentation.view.authentication.LoginScreen
import com.example.shopna.presentation.view.home.updateLocale
import com.example.shopna.presentation.view.landing.OnBoardingScreen
import com.example.shopna.presentation.view.landing.SplashScreen
import com.example.shopna.ui.theme.ShopnaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopnaTheme {
                var isSplashFinished by remember { mutableStateOf(false) }
                val sharedPreferences =this.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                val languageCode = java.util.Locale.getDefault().language
                updateLocale(this,sharedPreferences.getString("langCode",languageCode)?:languageCode)
                if (!isSplashFinished) {
                    SplashScreen { isSplashFinished = true }
                } else {
                    if (isOnBoardingShown()) {
                        Navigator(LoginScreen(LocalContext.current))
                    } else {
                        Navigator(OnBoardingScreen())
                    }
                }
            }
        }
    }

    private fun isOnBoardingShown(): Boolean {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("on_boarding_shown", false)
    }
}