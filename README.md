# Sankar Group Assignment

A modern Android application built using Jetpack Compose that allows users to view their call history, access their contacts, and make phone calls directly from the app. 

## Features

* **Call History:** Displays a detailed list of the user's call logs, including caller name, phone number, call type (Incoming, Outgoing, Missed, Rejected), date, and duration.
* **Contacts Integration:** Fetches and displays the user's contact list in alphabetical order. 
* **Direct Dialing:** Users can initiate phone calls directly from the contacts list or the dialer screen.
* **Bottom Navigation:** A smooth and intuitive bottom navigation bar to switch between History, Call, and Contacts screens.
* **Runtime Permissions:** Handles sensitive permissions (Read Call Log, Read Contacts, Call Phone) gracefully within the Compose UI.

## Tech Stack

* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for a modern, declarative UI.
* **Architecture Pattern:** MVVM (Model-View-ViewModel) architecture.
* **Navigation:** Jetpack Navigation Compose for seamless routing between screens.
* **Asynchronous Programming:** Kotlin Coroutines & Flow (`StateFlow`) for background tasks and state management.
* **Content Providers:** Utilizes `CallLog.Calls.CONTENT_URI` and `ContactsContract` to query device data.

## Project Structure

* `MainActivity.kt`: The main entry point of the app that sets up the Compose navigation.
* `appViewModel.kt`: An `AndroidViewModel` responsible for fetching and managing call logs and contact data asynchronously.
* `screen/`
  * `NAVIGATION/navApp.kt`: Contains the `NavHost`, Bottom Navigation Bar, and route definitions.
  * `CallHistoryScreen.kt`: The UI for requesting call log permissions and displaying the call history.
  * `contactScreen.kt`: The UI for requesting contact permissions and displaying the contact list.
  * `callScreen.kt`: The UI for dialing numbers and initiating phone calls.
* `models/`
  * `callLogItem.kt`: Data class representing a single call log entry.
  * `contactItem.kt`: Data class representing a single contact.

## Permissions Required

The app requests the following dangerous permissions at runtime:
* `android.permission.READ_CALL_LOG`: To display the call history.
* `android.permission.READ_CONTACTS`: To display the contact list.
* `android.permission.CALL_PHONE`: To allow the user to make phone calls directly.

## Getting Started

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or a physical device.
4. Grant the requested permissions when prompted to see the data populate.
