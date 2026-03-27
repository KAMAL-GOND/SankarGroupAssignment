package com.example.sankargroupassignment.screen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.content.ContextCompat
import com.example.sankargroupassignment.appViewModel

@Composable
fun CallScreen(veiwModel: appViewModel){
    //var state = veiwModel.stateFlow.collectAsState()
    var number by remember { mutableStateOf("") }
    var ScreenOn by remember{ mutableStateOf(ContextCompat.checkSelfPermission(veiwModel.context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) }
    var contract = ActivityResultContracts.RequestPermission().apply {
        Manifest.permission.CALL_PHONE
    }
    var Local = LocalContext.current
     var activity = Local.findActivity()
    var request = rememberLauncherForActivityResult(
        contract= contract
    ) { it->
        if(it){
           // veiwModel.readCallLogs()
            ScreenOn = true

        }
        else{
            Toast.makeText(Local,"permission not granted", Toast.LENGTH_LONG).show()
        }

    }
    LaunchedEffect(Unit) {
        if(ContextCompat.checkSelfPermission(veiwModel.context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            request.launch(Manifest.permission.CALL_PHONE)
        }
        else{
            ScreenOn = true

        }
    }

    if(!ScreenOn){
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            Text("To call give us permissions")
            Button({request.launch(Manifest.permission.CALL_PHONE)}) {
                Text("Allow Permission")
            }
        }
    }
    else{
        LocalSoftwareKeyboardController.current?.show()
        Column(modifier = Modifier.fillMaxSize()){
            OutlinedTextField(
                value = number,
                onValueChange = {number=it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone,),
                enabled = true,



            )
            Button(onClick = {
                onCall(activity!!,number.toString())
            }, colors = ButtonColors(
                contentColor = Color.White,
                containerColor = Color.Green,
                disabledContainerColor = Color.Green,
                disabledContentColor =  Color.White,
            )) {
                Text("CALL")
            }

        }
    }
}
fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
fun onCall(Activity : Activity, number: String){
    val intent = Intent(Intent.ACTION_CALL)
    intent.data=Uri.parse("tel:$number")
    Activity.startActivity(intent)
}