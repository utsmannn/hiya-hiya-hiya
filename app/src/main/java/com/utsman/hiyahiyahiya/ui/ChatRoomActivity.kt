package com.utsman.hiyahiyahiya.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.data.UserPref
import com.utsman.hiyahiyahiya.di.network
import com.utsman.hiyahiyahiya.model.*
import com.utsman.hiyahiyahiya.network.NetworkInstance
import com.utsman.hiyahiyahiya.network.NetworkMessage
import com.utsman.hiyahiyahiya.network.TypeMessage
import com.utsman.hiyahiyahiya.ui.adapter.ChatAdapter
import com.utsman.hiyahiyahiya.ui.viewmodel.ChatViewModel
import com.utsman.hiyahiyahiya.utils.click
import com.utsman.hiyahiyahiya.utils.logi
import com.utsman.hiyahiyahiya.utils.longToast
import com.utsman.hiyahiyahiya.utils.toast
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ChatRoomActivity : AppCompatActivity() {
    private val chatRoomViewModel: ChatViewModel by viewModel()
    private val network: NetworkMessage by network()
    private val chatAdapter: ChatAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        val room = intent.getParcelableExtra<RowChat.ChatRoom>("room")
        val to = intent.getStringExtra("to")
        if (room != null) {
            setupView(room, to)
        }
    }

    private fun setupView(room: RowChat.ChatRoom, to: String?) {
        val linearLayoutManager = LinearLayoutManager(this)
        rv_chat.run {
            layoutManager = linearLayoutManager
            adapter = chatAdapter
        }

        logi("open room ---> ${room.id}")
        chatRoomViewModel.chats(roomId = room.id).observe(this, Observer {
            if (it.isEmpty()) chatAdapter.addChat(listOf(RowChatItem.Empty("chat is empty")))
            else chatAdapter.addChat(it)
        })

        /*chatRoomViewModel.allChats.observe(this, Observer {
            if (it.isEmpty()) chatAdapter.addChat(listOf(RowChatItem.Empty("chat is empty")))
            else chatAdapter.addChat(it)
        })*/

        btn_send_message.click(lifecycleScope) {
            val chat = chatItem {
                this.id = UUID.randomUUID().toString()
                this.from = UserPref.getUserId()
                this.message = in_chat_message.text.toString()
                this.roomId = room.id
                this.time = System.currentTimeMillis()
                this.to = to
            }

            GlobalScope.launch {
                chatRoomViewModel.insert(chat.toLocalChat())
            }

            val messageBody = messageBody {
                this.fromMessage = chat.from
                this.toMessage = chat.to
                this.typeMessage = TypeMessage.MESSAGE
                this.payload = chat.toLocalChat()
            }

            network.send(this@ChatRoomActivity, messageBody, object : NetworkMessage.MessageCallback {
                override fun onSuccess() {
                    toast("success send")
                }

                override fun onFailed(message: String?) {
                    longToast("failed")
                    logi("failed ------> $message")
                }

            })

            logi("try sending ----> $messageBody")
            in_chat_message.setText("")
        }
    }
}