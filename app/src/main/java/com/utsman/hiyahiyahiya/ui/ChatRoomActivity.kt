package com.utsman.hiyahiyahiya.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.data.UserPref
import com.utsman.hiyahiyahiya.database.LocalUserDatabase
import com.utsman.hiyahiyahiya.di.network
import com.utsman.hiyahiyahiya.model.*
import com.utsman.hiyahiyahiya.network.NetworkMessage
import com.utsman.hiyahiyahiya.model.TypeMessage
import com.utsman.hiyahiyahiya.ui.adapter.ChatAdapter
import com.utsman.hiyahiyahiya.ui.viewmodel.ChatViewModel
import com.utsman.hiyahiyahiya.ui.viewmodel.RoomViewModel
import com.utsman.hiyahiyahiya.utils.*
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ChatRoomActivity : AppCompatActivity() {
    private val chatAdapter: ChatAdapter by inject()
    private val localUserDb: LocalUserDatabase by inject()
    private val chatRoomViewModel: ChatViewModel by viewModel()
    private val roomViewModel: RoomViewModel by viewModel()
    private val network: NetworkMessage by network()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        val room = intent.getParcelableExtra<RowRoom.RoomItem>("room")
        registerBroadcast(room)
        setupToolbar(room)

        val to = intent.getStringExtra("to")
        if (room != null) {
            setupView(room, to)
        }
    }

    private fun setupToolbar(room: RowRoom.RoomItem?, subtitle: String? = null) {
        img_toolbar.load(room?.imageRoom, isCircle = true)
        tx_title.text = room?.titleRoom
        if (subtitle != null) {
            tx_title.animate().start()
            tx_subtitle.animate()
                .alpha(1f)
                .setDuration(500)
                .withStartAction {
                    tx_subtitle.text = subtitle
                    tx_subtitle.visibility = View.VISIBLE
                }
                .start()
        } else {
            tx_title.animate().start()
            tx_subtitle.animate()
                .alpha(0f)
                .setDuration(500)
                .withEndAction {
                    tx_subtitle.visibility = View.GONE
                }
                .start()
        }
    }

    private fun registerBroadcast(roomItem: RowRoom.RoomItem?) {
        Broadcast.with(GlobalScope).observer { key, _ ->
            if (key == "typing_${roomItem?.id}") {
                GlobalScope.launch {
                    runOnUiThread {
                        setupToolbar(roomItem, "Typing...")
                    }
                    delay(1000)
                    runOnUiThread {
                        setupToolbar(roomItem)
                    }
                }
            }
        }
    }

    private fun setupView(roomItem: RowRoom.RoomItem, to: String?) {
        val linearLayoutManager = LinearLayoutManager(this)
        rv_chat.run {
            layoutManager = linearLayoutManager
            adapter = chatAdapter
        }

        chatRoomViewModel.chats(roomId = roomItem.id).observe(this, Observer {
            if (it.isEmpty()) chatAdapter.addChat(listOf(RowChatItem.Empty("chat is empty")))
            else chatAdapter.addChat(it)
        })

        in_chat_message.debounce(700) {
            val typingBody = typingBody {
                this.ownerId = UserPref.getUserId()
                this.roomId = roomItem.id
            }

            val messageBody = messageBody {
                this.fromMessage = UserPref.getUserId()
                this.toMessage = to
                this.typeMessage = TypeMessage.TYPING
                this.payload = typingBody
            }

            GlobalScope.launch {
                network.send(messageBody, object : NetworkMessage.MessageCallback {
                    override fun onSuccess() {
                    }

                    override fun onFailed(message: String?) {
                    }
                })
            }
        }

        btn_send_message.click(lifecycleScope) {
            val messageString = in_chat_message.text.toString()

            roomItem.subtitleRoom = messageString
            roomItem.localChatStatus = LocalChatStatus.SEND

            GlobalScope.launch {
                val currentUser = localUserDb.localUserDao().localUser(UserPref.getUserId())

                val chat = chatItem {
                    this.id = UUID.randomUUID().toString()
                    this.from = UserPref.getUserId()
                    this.message = messageString
                    this.roomId = roomItem.id
                    this.time = System.currentTimeMillis()
                    this.to = to
                    this.currentUser = currentUser
                }

                chatRoomViewModel.insert(chat.toLocalChat())
                roomViewModel.insert(roomItem.toLocalRoom())

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

                runOnUiThread {
                    logi("try sending ----> $messageBody")
                    toast(currentUser?.name)
                    in_chat_message.setText("")
                }

            }
        }
    }
}