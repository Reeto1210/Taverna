package com.mudryakov.taverna.ui.Objects

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.mudryakov.taverna.R
import com.mudryakov.taverna.models.CommonModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_settings.*

fun AppCompatActivity.replaceActivity(newAct:Activity) {
    val i = Intent(this,newAct::class.java)
    startActivity(i)
this.finish()
}
fun showToast(mesg:String){
    Toast.makeText(APP_ACTIVITY,mesg, Toast.LENGTH_LONG).show()
}
fun Fragment.changeFragment(newFragment:Fragment){
    fragmentManager?.beginTransaction()
        ?.replace(R.id.RegisterContainer, newFragment)
        ?.addToBackStack(null)
        ?.commit()
}
fun AppCompatActivity.changeFragment(newFragment: Fragment, addStack:Boolean = true){
    if (addStack){
    supportFragmentManager.beginTransaction()
        .replace(R.id.dataConteiner,newFragment)
        ?.addToBackStack(null)
        ?.commit()}
    else {
        supportFragmentManager.beginTransaction()
            .replace(R.id.dataConteiner,newFragment)
            ?.commit()
    }
}
fun ImageView.downloadAndSetImage(url:String){
           Picasso.get()
            .load(url)
            .placeholder(R.drawable.bomjara)
               .fit()
               .into(this)

}
fun hideKeyBoard() {
    val imm: InputMethodManager =
        APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken, 0)
}
fun setFullnameUi():String{
    if (USER.fullName.isEmpty()) return "Unknown"
    if (USER.fullName.contains(" ")) {
        val name = USER.fullName.split(" ")[0].capitalize()
        val surName = USER.fullName.split(" ")[1].capitalize()
        return "$name $surName"
    } else return  USER.fullName.capitalize()

}
fun DataSnapshot.getCommonModel() = this.getValue(CommonModel::class.java) ?: CommonModel()