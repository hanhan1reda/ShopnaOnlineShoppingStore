package com.example.shopna.presentation.view.home

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.shopna.R
import com.example.shopna.data.network.RetrofitInstance
import com.example.shopna.ui.theme.kPrimaryColor
import java.util.*

class LanguageScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val sharedPreferences =context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val langCode = sharedPreferences.getString("langCode", "en")

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.change_language),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = if (langCode == "ar") Icons.Default.KeyboardArrowRight else Icons.Default.KeyboardArrowLeft,
                                contentDescription = "back",
                                tint = kPrimaryColor
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),

                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        updateLocale(context, "ar")
                        (context as? Activity)?.recreate()
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(kPrimaryColor),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                ) {
                    Text(
                        text = stringResource(id = R.string.arabic),
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        updateLocale(context, "en")
                        (context as? Activity)?.recreate()
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(kPrimaryColor),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                ) {
                    Text(
                        text = stringResource(id = R.string.english),
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}


fun updateLocale(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val resources = context.resources
    val config = resources.configuration
    config.setLocale(locale)
    context.createConfigurationContext(config)
    resources.updateConfiguration(config, resources.displayMetrics)
    val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("langCode", languageCode)
    editor.apply()
    RetrofitInstance.setLanguage(languageCode)
}
