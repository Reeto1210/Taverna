package com.mudryakov.taverna.ui.Fragmets.Groups

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.Objects.changeFragment
import com.mudryakov.taverna.Objects.showToast
import com.mudryakov.taverna.Objects.sizeOfMembers
import com.mudryakov.taverna.Objects.startCrop
import com.mudryakov.taverna.R
import com.mudryakov.taverna.appDatabaseHelper.APP_ACTIVITY
import com.mudryakov.taverna.appDatabaseHelper.createGroup
import com.mudryakov.taverna.ui.Fragmets.BaseFragment
import com.mudryakov.taverna.ui.Fragmets.MainChatList.MainFragment
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_group_greate.*
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil.showKeyboard


class GroupGreateFragment() : BaseFragment(R.layout.fragment_group_greate) {
    var  photoUri: Uri? = null
    lateinit var mAadapter: GroupGreateAdapter
    lateinit var mRecycler: RecyclerView


    override fun onResume() {
        super.onResume()
initClickers()
        initCommonFields()
        initRecycle()
    }

    private fun initClickers() {
      groupAddPhoto.setOnClickListener {


          startCrop()}
        groupGreate.setOnClickListener {
            val name = groupAddName.text.toString()
            if (name.isNotEmpty()){
            createGroup(photoUri,name){
                showToast("группа успешно создана")
                changeFragment(MainFragment())
            }
            } else showToast("Введите название группы")
        }
    }



    private fun initRecycle() {
        mRecycler = greateGroupRecycle
        mAadapter = GroupGreateAdapter()
        mRecycler.layoutManager = LinearLayoutManager(this.context)
        mRecycler.adapter = mAadapter
    }

    private fun initCommonFields() {
        groupNumberOfMembers.text = sizeOfMembers(GroupsAddAdapter.listUsersForGroup.size)
        showKeyboard(APP_ACTIVITY, groupAddName)
        APP_ACTIVITY.title = "Создать группу"

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null
            && resultCode == Activity.RESULT_OK
        ) {
            photoUri = CropImage.getActivityResult(data).uri

     groupAddPhoto.setImageURI(photoUri)



            }
        }




}