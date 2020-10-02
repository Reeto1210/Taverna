package com.mudryakov.taverna.ui.Objects

import android.net.Uri
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mudryakov.taverna.activityes.RegisterActivity
import com.mudryakov.taverna.models.CommonModel
import com.mudryakov.taverna.models.Users

lateinit var APP_ACTIVITY: AppCompatActivity
lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER: Users
lateinit var CURRENT_UID: String

const val NODE_PHONES_CONTACTS = "phone_contacts"
const val NODE_PHONES = "phones"
const val NODE_PROFILE_IMG = "profileImg"
const val NODE_USERNAMES = "usernames"
const val NODE_USERS = "users"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phoneNumber"
const val CHILD_USERNAME = "username"
const val CHILD_STATUS = "status"
const val CHILD_FULL_NAME = "fullName"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_BIO = "bio"


fun initFireBase() {
    AUTH = FirebaseAuth.getInstance()

    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = Users()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}

inline fun putImageToStorage(path: StorageReference, uri: Uri, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast("Произошла ошибка") }

}

inline fun downloadUrl(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast("Произошла ошибка") }
}

inline fun addUrlBase(url: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .child(CHILD_PHOTO_URL)
        .setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast("Произошла ошибка") }
}

inline fun initUser(crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .addListenerForSingleValueEvent(appValueEventListener {
                USER = it.getValue(Users::class.java) ?: Users()
            function()
        })
}

fun initContacts() {
   if (AUTH.currentUser!=null) {
       if (checkPermission(READ_CONTACTS)) {
           var arrayContacts = arrayListOf<CommonModel>()
           val cursor = APP_ACTIVITY.contentResolver.query(
               ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
               null,
               null,
               null,
               null
           )
           cursor?.let {
               while (it.moveToNext()) {
                   val fullName =
                       it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                   val phone =
                       it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                           .replace(Regex("[\\s,-]"), "").replace("8", "+7")
                   val newModel = CommonModel(fullName = fullName, phoneNumber = phone)
                   arrayContacts.add(newModel)
               }
           }
           cursor?.close()
           REF_DATABASE_ROOT.child(NODE_PHONES)
               .addListenerForSingleValueEvent(appValueEventListener {
                   it.children.forEach { snapshot ->
                       arrayContacts.forEach { contact ->
                           if (snapshot.key == contact.phoneNumber) {
                               REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS)
                                   .child(CURRENT_UID)
                                   .child(snapshot.value.toString())
                                   .child(CHILD_ID).setValue(snapshot.value.toString())
                               REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS)
                                   .child(CURRENT_UID)
                                   .child(snapshot.value.toString())
                                   .child(CHILD_FULL_NAME).setValue(contact.fullName)

                           }

                       }

                   }
               })
       }
   }
}