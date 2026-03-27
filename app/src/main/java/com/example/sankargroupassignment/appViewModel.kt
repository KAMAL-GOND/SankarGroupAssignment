package com.example.sankargroupassignment

import android.app.Application
import android.content.Context
import android.provider.CallLog
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sankargroupassignment.models.callLogItem
import com.example.sankargroupassignment.models.contactItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class appViewModel(application: Application) : AndroidViewModel(application) {

    private var state = MutableStateFlow(result())
    var stateFlow = state.asStateFlow()
    
    // Use getApplication<Application>().applicationContext
    val context: Context
        get() = getApplication<Application>().applicationContext

    fun readCallLogs() = viewModelScope.launch {
        try {
            var callHistoryList = mutableListOf<callLogItem>()

            var table = context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                CallLog.Calls.DATE + " DESC" // for odering in latest to past
            )

            table?.use {
                val name = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
                val callType = it.getColumnIndex(CallLog.Calls.TYPE)
                val date = it.getColumnIndex(CallLog.Calls.DATE)
                val phoneNumber = it.getColumnIndex(CallLog.Calls.NUMBER)
                val duration = it.getColumnIndex(CallLog.Calls.DURATION)
                while (it.moveToNext()) {
                    callHistoryList.add(
                        callLogItem(
                            name = it.getString(name),
                            phoneNumber = it.getString(phoneNumber),
                            Date = it.getString(date),
                            callType = when (it.getInt(callType)) {
                                CallLog.Calls.INCOMING_TYPE -> "Incoming"
                                CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                                CallLog.Calls.MISSED_TYPE -> "Missed Call"
                                CallLog.Calls.REJECTED_TYPE -> "Rejected"
                                else -> "Unkown Type"
                            },
                            duration = it.getString(duration)
                        )
                    )
                }
            }
            state.value = result(Loading = false, success = callHistoryList)
            Log.d("callHistory", callHistoryList.toString())
        } catch (e: Exception) {
            state.value = result(Loading = false, error = e.toString())
        }
    }

    private var Contactstate = MutableStateFlow(result2())
    var ContactstateFlow = Contactstate.asStateFlow()

    fun readContacts() = viewModelScope.launch {
        try {
            val contacts = mutableListOf<contactItem>()

            val resolver = context.contentResolver

            val cursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )

            cursor?.use {
                val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                while (it.moveToNext()) {
                    val name = it.getString(nameIndex)
                    val number = it.getString(numberIndex)

                    contacts.add(contactItem(name, number))
                }
            }
            Contactstate.value = result2(Loading = false, success = contacts)

        } catch (e: Exception) {
            Contactstate.value = result2(Loading = false, error = e.toString())
        }
    }
}

data class result(
    var success: List<callLogItem>? = null,
    var error: String? = null,
    var Loading: Boolean = true
)

data class result2(
    var success: List<contactItem>? = null,
    var error: String? = null,
    var Loading: Boolean = true
)
