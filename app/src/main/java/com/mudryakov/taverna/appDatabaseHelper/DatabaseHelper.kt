package com.mudryakov.taverna.appDatabaseHelper

import android.net.Uri
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.mudryakov.taverna.models.CommonModel
import com.mudryakov.taverna.models.Users
import com.mudryakov.taverna.Objects.READ_CONTACTS
import com.mudryakov.taverna.Objects.appValueEventListener
import com.mudryakov.taverna.Objects.checkPermission
import com.mudryakov.taverna.Objects.showToast

lateinit var TOOLBAR: androidx.appcompat.widget.Toolbar
lateinit var APP_ACTIVITY: AppCompatActivity
lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER: Users
lateinit var CURRENT_UID: String


const val NODE_IMAGES = "images"
const val TYPE_IMAGE = "image"
const val TYPE_TEXT = "text"
const val NODE_PHONES_CONTACTS = "phone_contacts"
const val NODE_PHONES = "phones"
const val NODE_PROFILE_IMG = "profileImg"
const val NODE_USERNAMES = "usernames"
const val NODE_USERS = "users"
const val NODE_MESSAGES = "messages"

const val CHILD_TIME = "time"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_TEXT = "text"
const val CHILD_ID = "id"
const val CHILD_PHONE = "phoneNumber"
const val CHILD_USERNAME = "username"
const val CHILD_STATUS = "status"
const val CHILD_FULL_NAME = "fullName"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_BIO = "bio"
const val CHILD_IMAGE = "imageUrl"


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
    if (AUTH.currentUser != null) {
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

fun sendMessage(
    text: String,
    friendId: String,
    type: String,
    imageUrl: String = "",
    function: () -> Unit
) {
    val refUser = "/$NODE_MESSAGES/$CURRENT_UID/$friendId"
    val refFriend = "/$NODE_MESSAGES/$friendId/$CURRENT_UID"
    val key = REF_DATABASE_ROOT.child(refUser).push().key

    val addMessage = HashMap<String, Any>()
    addMessage[CHILD_TEXT] = text
    addMessage[CHILD_FROM] = CURRENT_UID
    addMessage[CHILD_TYPE] = type
    addMessage[CHILD_TIME] = ServerValue.TIMESTAMP
    addMessage[CHILD_IMAGE] = imageUrl
    addMessage[CHILD_ID] = key.toString()
    val hashForUpdate = HashMap<String, Any>()
    hashForUpdate["$refUser/$key"] = addMessage
    hashForUpdate["$refFriend/$key"] = addMessage

    REF_DATABASE_ROOT.updateChildren(hashForUpdate)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}