package com.mudryakov.taverna.ui.Fragmets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mudryakov.taverna.R
import com.mudryakov.taverna.databinding.FragmentChatBinding
import com.mudryakov.taverna.ui.Objects.APP_ACTIVITY


class ChatFragment : Fragment(R.layout.fragment_chat) {


    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Taverna"
    }
}