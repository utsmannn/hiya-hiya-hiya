package com.utsman.hiyahiyahiya.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.di.network
import com.utsman.hiyahiyahiya.network.NetworkMessage
import com.utsman.hiyahiyahiya.utils.click
import kotlinx.android.synthetic.main.fragment_chats.view.*
import kotlinx.coroutines.MainScope

class ChatsFragment : Fragment() {
    private val networkMessage: NetworkMessage by network()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.run {
            setupView()
        }
    }

    private fun View.setupView() {

    }
}
