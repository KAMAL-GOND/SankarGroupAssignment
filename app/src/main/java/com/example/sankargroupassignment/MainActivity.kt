package com.example.sankargroupassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sankargroupassignment.screen.CallHistoryScreen
import com.example.sankargroupassignment.ui.theme.SankarGroupAssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var model = appViewModel(this.applicationContext)
        model.readCallLogs()
        setContent {
            SankarGroupAssignmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {it-> CallHistoryScreen(model) }

            }
        }
    }
}

