package com.example.sankargroupassignment.screen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.sankargroupassignment.appViewModel
import com.example.sankargroupassignment.models.contactItem

@Composable
fun ContactScreen(viewModel: appViewModel) {
    val state by viewModel.ContactstateFlow.collectAsState()
    val context = LocalContext.current
    val activity = context.findActivity()


    var hasPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                hasPermission = true
            } else {
                Toast.makeText(context, "Permission not granted , please allow us to read contacts", Toast.LENGTH_LONG).show()
            }
        }
    )


    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            viewModel.readContacts()
        }
        else{
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    if (hasPermission) {

        when {
            state.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.error}")
                }
            }
            else -> {
                val items = state.success
                if (items != null) {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(15.dp)) {
                        items.forEach {
                            item {
                                singleContactItem(it, activity!!)
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }
        }
    } else {

        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ) {
            Text("To watch contacts provide us permission to read contacts")
            Button(onClick = { permissionLauncher.launch(Manifest.permission.READ_CONTACTS) }) {
                Text("Allow Permission")
            }


        }
    }
}

@Composable
fun singleContactItem(item: contactItem, activity: Activity) {
    Card(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(color = Color.LightGray)) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) { // Added padding for better visuals
            Text(item.name.toString())
            Text(item.number)
            Button(onClick = { onCall(activity, item.number.toString()) }) {
                Text("call")
            }
        }
    }
}