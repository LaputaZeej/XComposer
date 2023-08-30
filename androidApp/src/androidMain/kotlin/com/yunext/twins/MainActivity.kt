package com.yunext.twins

import MainView
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yunext.twins.data.Bird
import com.yunext.twins.ui.AppDestination
import com.yunext.twins.ui.isRoot
import com.yunext.twins.ui.page.PageStateHolder

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()
            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons,
                    isNavigationBarContrastEnforced = false,
                )
            }
            Scaffold {
                val page: AppDestination by PageStateHolder.state.collectAsState()
                MainView(it)

                BackHandler {
                    if (page.isRoot()) {
                        finish()
                    } else {
                        PageStateHolder.back()
                    }
                }
            }
        }
        Bird.test()
    }
}