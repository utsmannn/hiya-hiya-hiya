package com.utsman.hiyahiyahiya.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.utils.click
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.run {
            setupView()
        }
    }

    private fun View.setupView() {
        btn_send_message.click(lifecycleScope) {

        }
    }
}
