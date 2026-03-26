package com.example.sankargroupassignment

import android.R
import android.content.Context
import android.provider.CallLog
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sankargroupassignment.models.callLogItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class appViewModel (context: Context): ViewModel() {

    private var state = MutableStateFlow(result())
    var stateFlow = state.asStateFlow()
    var Context = context

    fun readCallLogs() = viewModelScope.launch {
        try{

        var callHistoryList= mutableListOf<callLogItem>()

       var table= Context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            CallLog.Calls.DATE+" DESC" // for odering in latest to past
        )

      table?.use {
          val name=it.getColumnIndex(CallLog.Calls.CACHED_NAME)
          val callType =it.getColumnIndex(CallLog.Calls.TYPE)
          val date = it.getColumnIndex(CallLog.Calls.DATE)
          val phoneNumber=it.getColumnIndex(CallLog.Calls.NUMBER)
          val duration=it.getColumnIndex(CallLog.Calls.DURATION)
          while (it.moveToNext()){
              callHistoryList.add(callLogItem(
                  name = it.getString(name),
                  phoneNumber = it.getString(phoneNumber),
                  Date = it.getString(date),
                  callType = when(it.getInt(callType)){
                      CallLog.Calls.INCOMING_TYPE-> "Incoming";
                      CallLog.Calls.OUTGOING_TYPE->"Outgoing";
                      CallLog.Calls.MISSED_TYPE->"Missed Call"
                      CallLog.Calls.REJECTED_TYPE->"Rejected"
                      else -> "Unkown Type"
                  },
                  duration = it.getString(duration)

              ))
          }
      }
        state.value= result(Loading = false, success = callHistoryList)



        Log.d("callHostory",callHistoryList.toString())}
        catch (e: Exception){
            state.value= result(Loading = false, error = e.toString())
        }
    }
}

data class result(
    var success: List<callLogItem>?=null,
    var error: String?=null,
    var Loading : Boolean=true
)