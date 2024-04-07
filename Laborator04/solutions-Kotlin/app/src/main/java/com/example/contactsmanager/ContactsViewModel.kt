package com.example.contactsmanager

import android.content.ContentValues
import android.content.Intent
import android.provider.ContactsContract
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/*
* ViewModel for the ContactsManagerUI composable
* https://developer.android.com/topic/libraries/architecture/viewmodel
 */
class ContactsViewModel : ViewModel() {
    /*
    * MutableState variables to hold the values of the input fields
    * In real-world apps, you should use LiveData to observe changes in the data and update the UI
    * https://developer.android.com/topic/libraries/architecture/livedata
    */
    var name by mutableStateOf("")
    var phone by mutableStateOf("")
    var email by mutableStateOf("")
    var address by mutableStateOf("")
    var jobTitle by mutableStateOf("")
    var company by mutableStateOf("")
    var website by mutableStateOf("")
    var im by mutableStateOf("")
    var showAdditionalFields by mutableStateOf(false)

    fun saveContact(
        name: String?,
        phone: String?,
        email: String?,
        address: String?,
        jobTitle: String?,
        company: String?,
        website: String?,
        im: String?,
        launcher: ActivityResultLauncher<Intent>

    ) {
        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
            name?.let { putExtra(ContactsContract.Intents.Insert.NAME, it) }
            phone?.let { putExtra(ContactsContract.Intents.Insert.PHONE, it) }
            email?.let { putExtra(ContactsContract.Intents.Insert.EMAIL, it) }
            address?.let { putExtra(ContactsContract.Intents.Insert.POSTAL, it) }
            jobTitle?.let { putExtra(ContactsContract.Intents.Insert.JOB_TITLE, it) }
            company?.let { putExtra(ContactsContract.Intents.Insert.COMPANY, it) }

            val contactData = arrayListOf<ContentValues>()
            website?.let {
                val websiteRow = ContentValues().apply {
                    put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                    put(ContactsContract.CommonDataKinds.Website.URL, website)
                }
                contactData.add(websiteRow)
            }
            im?.let {
                val imRow = ContentValues().apply {
                    put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                    put(ContactsContract.CommonDataKinds.Im.DATA, im)
                }
                contactData.add(imRow)
            }
            if (contactData.isNotEmpty()) {
                putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData)
            }
        }
        launcher.launch(intent)

    }

    fun clearFields() {
        name = ""
        phone = ""
        email = ""
        address = ""
        jobTitle = ""
        company = ""
        website = ""
        im = ""
    }
}