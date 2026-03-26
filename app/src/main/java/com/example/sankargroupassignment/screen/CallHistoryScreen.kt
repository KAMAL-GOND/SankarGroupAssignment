package com.example.sankargroupassignment.screen

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.example.sankargroupassignment.appViewModel
import com.example.sankargroupassignment.models.callLogItem
import java.security.Permission

@Composable
fun CallHistoryScreen(veiwModel: appViewModel){
    var state = veiwModel.stateFlow.collectAsState()
    var contract = ActivityResultContracts.RequestPermission().apply {
        Manifest.permission.READ_CALL_LOG
    }
    var Local = LocalContext.current
    var request = rememberLauncherForActivityResult(
        contract= contract
    ) { it->
        if(it){
            veiwModel.readCallLogs()

        }
        else{
            Toast.makeText(Local,"permission not granted", Toast.LENGTH_LONG).show()
        }

    }
    LaunchedEffect(Unit) {
        if(ContextCompat.checkSelfPermission(veiwModel.Context, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED){
            request.launch(Manifest.permission.READ_CALL_LOG)
            }
        else{
            veiwModel.readCallLogs()
        }
    }

    if(ContextCompat.checkSelfPermission(veiwModel.Context, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED){
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            Text("To watch call History provide us permission to read call logs")
            Button({request.launch(Manifest.permission.READ_CALL_LOG)}) {
                Text("Allow Permission")
            }
        }
    }
    else{
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            when{
                state.value.Loading -> CircularProgressIndicator()
                state.value.success!=null-> {
                    var Items = state.value.success as List<callLogItem>
                    LazyColumn() {
                        items(Items.size){
                            Items.forEach { singleItem(it) }

                        }
                    }
                }
            }
        }
    }



}

@Composable
fun singleItem( item: callLogItem){
    Card (modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(color = Color.LightGray)) {
        Column() {
            Text(item.name.toString())
            Text(item.phoneNumber)
            Text(item.Date)
            Text(item.callType)
            Text(item.duration)

        }
    }
}