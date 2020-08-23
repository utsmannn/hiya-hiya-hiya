package com.utsman.hiyahiyahiya.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.vanniktech.emoji.EmojiPopup
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ChatRoomActivity : AppCompatActivity(), KeyboardVisibilityListener {
    private val chatAdapter: ChatAdapter by inject()
    private val localUserDb: LocalUserDatabase by inject()
    private val chatRoomViewModel: ChatViewModel by viewModel()
    private val roomViewModel: RoomViewModel by viewModel()
    private val network: NetworkMessage by network()

    private var isReachBottom = true
    private var chatSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        setKeyboardVisibilityListener(parent_layout)

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
                    delay(3000)
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
            if (it.isEmpty()) {
                chatAdapter.addChat(listOf(RowChatItem.Empty("chat is empty")))
            } else {
                chatSize = it.size
                chatAdapter.addChat(it)
                rv_chat.scrollToPosition(it.lastIndex)
                setupRecyclerViewScrollListener(linearLayoutManager, it.lastIndex)
                isReachBottom = true
            }
        })

        setupInputText(roomItem, to)

        btn_send_message.click(lifecycleScope) {
            sendMessage(roomItem, to)
        }
    }

    private fun setupInputText(roomItem: RowRoom.RoomItem, to: String?) {
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


        // final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(rootView).build(emojiEditText);
        //emojiPopup.toggle(); // Toggles visibility of the Popup.
        //emojiPopup.dismiss(); // Dismisses the Popup.
        //emojiPopup.isShowing();

        val emojiPopup = EmojiPopup.Builder
            .fromRootView(parent_layout)
            .build(in_chat_message)

        btn_emoticon.click {
            if (emojiPopup.isShowing) {
                emojiPopup.dismiss()
                btn_emoticon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_round_insert_emoticon_24))
            } else {
                emojiPopup.toggle()
                btn_emoticon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_keyboard_24))
            }
        }
    }

    private fun sendMessage(roomItem: RowRoom.RoomItem, to: String?) {
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

            network.send(messageBody, object : NetworkMessage.MessageCallback {
                override fun onSuccess() {
                    logi("success")
                }

                override fun onFailed(message: String?) {
                    logi("failed ------> $message")
                }
            })

            runOnUiThread {
                in_chat_message.setText("")
            }
        }
    }

    private fun setupRecyclerViewScrollListener(layoutManager: LinearLayoutManager, lastIndexList: Int) {
        rv_chat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemLastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                isReachBottom = itemLastPosition == lastIndexList
            }
        })
    }

    override fun onKeyboardVisibilityChanged(keyboardVisible: Boolean) {
        if (chatSize > 1) {
            if (isReachBottom) rv_chat.scrollToPosition(chatSize-1)
        }
    }
}