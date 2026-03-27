package com.example.sankargroupassignment.screen.NAVIGATION

sealed class Routes(val route: String) {
    object CallScreen : Routes("call_screen")
    object Contacts : Routes("contacts_screen")
    object History : Routes("history_screen")
}
