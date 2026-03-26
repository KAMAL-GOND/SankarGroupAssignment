package com.example.sankargroupassignment.models

import kotlin.time.Duration

data class callLogItem(
    var name: String?=null,
    var phoneNumber: String,
    var callType:String,
    var Date:String,
    var duration: String
)
