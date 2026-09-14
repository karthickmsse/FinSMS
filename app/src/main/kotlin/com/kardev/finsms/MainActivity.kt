package com.kardev.finsms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.kardev.finsms.core.designsystem.FinSmsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinSmsTheme {
                Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background) {
                    val context = LocalContext.current
                    var hasPermissions by remember { mutableStateOf(hasSmsPermissions(context)) }

                    if (!hasPermissions) {
                        PermissionOnboardingScreen(onGranted = { hasPermissions = true })
                    } else {
                        val navController = rememberNavController()
                        Scaffold(bottomBar = { FinSmsBottomBar(navController) }) { padding ->
                            Box(Modifier.padding(padding)) {
                                FinSmsNavHost(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
