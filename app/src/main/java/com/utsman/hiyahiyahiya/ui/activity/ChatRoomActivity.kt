package com.utsman.hiyahiyahiya.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.data.ConstantValue
import com.utsman.hiyahiyahiya.data.UnreadPref
import com.utsman.hiyahiyahiya.data.UserPref
import com.utsman.hiyahiyahiya.database.LocalUserDatabase
import com.utsman.hiyahiyahiya.di.network
import com.utsman.hiyahiyahiya.model.features.UrlAttachment
import com.utsman.hiyahiyahiya.model.row.RowChatItem
import com.utsman.hiyahiyahiya.model.row.RowRoom
import com.utsman.hiyahiyahiya.model.types.LocalChatStatus
import com.utsman.hiyahiyahiya.model.types.TypeCamera
import com.utsman.hiyahiyahiya.model.types.TypeMessage
import com.utsman.hiyahiyahiya.model.utils.*
import com.utsman.hiyahiyahiya.network.NetworkMessage
import com.utsman.hiyahiyahiya.ui.adapter.ChatAdapter
import com.utsman.hiyahiyahiya.ui.adapter.chat_viewholder.AttachmentAdapter
import com.utsman.hiyahiyahiya.ui.viewmodel.ChatViewModel
import com.utsman.hiyahiyahiya.ui.viewmodel.RoomViewModel
import com.utsman.hiyahiyahiya.utils.*
import com.utsman.hiyahiyahiya.utils.url_utils.UrlFetcher
import com.utsman.hiyahiyahiya.utils.url_utils.UrlUtil
import com.vanniktech.emoji.EmojiPopup
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ChatRoomActivity : AppCompatActivity(), KeyboardVisibilityListener {
    private val chatAdapter: ChatAdapter by inject()
    private val attachAdapter: AttachmentAdapter by inject()
    private val localUserDb: LocalUserDatabase by inject()
    private val chatRoomViewModel: ChatViewModel by viewModel()
    private val roomViewModel: RoomViewModel by viewModel()
    private val network: NetworkMessage by network()

    private var isReachBottom = true
    private var chatSize = 0

    private var mUrlAttachment: UrlAttachment? = null

    private val room by lazy { intent.getParcelableExtra<RowRoom.RoomItem>("room") }
    private val to by lazy { intent.getStringExtra("to") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        setKeyboardVisibilityListener(parent_layout)

        showCardUrl(false)
        registerBroadcast(room)
        setupToolbar(room)

        if (room != null) {
            updateUnreadCount()
            disableNotificationInRoom()
            setupView(room, to)
        }
    }

    private fun updateUnreadCount() {
        UnreadPref.clearUnreadCount(room!!.id)
        GlobalScope.launch {
            roomViewModel.update(room!!.toLocalRoom())
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

    private fun disableNotificationInRoom() {
        Broadcast.with(GlobalScope).post("disable_notification", room)
    }

    private fun enableNotificationInRoom() {
        Broadcast.with(GlobalScope).post("enable_notification")
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
            setHasFixedSize(true)
        }


        chatRoomViewModel.chats(roomId = roomItem.id).observe(this, Observer {
            if (it.isNullOrEmpty()) {
                chatAdapter.addChat(listOf(RowChatItem.Empty("chat is empty")))
            } else {
                val newList = DividerCalculator.calculateDividerChat(it)
                chatSize = newList.size
                chatAdapter.addChat(newList)
                rv_chat.scrollToPosition(newList.lastIndex)
                mUrlAttachment = null
                setupRecyclerViewScrollListener()
            }

        })

        setupInputText(roomItem, to)
        setupAttachView(roomItem, to)

        in_chat_message.observerHeightChanges(this) {
            if (isReachBottom) rv_chat.scrollToPosition(chatSize - 1)
        }

        btn_send_message.click(lifecycleScope) {
            if (in_chat_message.text?.isEmpty() == false) {
                sendMessage(roomItem, to)
            }
        }

        btn_photo.click {
            intentTo(CameraActivity::class.java) {
                putExtra("room", roomItem)
                putExtra("to", to)
                putExtra("intent_type", TypeCamera.ATTACHMENT.name)
            }
        }

        btn_attach.click {
            showAttachLayout(!isAttachIsShown())
        }
    }

    private fun setupAttachView(roomItem: RowRoom.RoomItem, to: String?) {
        val gridLayoutManager = GridLayoutManager(this, 3)
        rv_attach.apply {
            layoutManager = gridLayoutManager
            adapter = attachAdapter
        }

        attachAdapter.addItems(ConstantValue.itemAttachments)
        attachAdapter.onAttachClick {
            if (isAttachIsShown()) showAttachLayout(false)
            when (it.id) {
                "camera" -> {
                    intentTo(CameraActivity::class.java) {
                        putExtra("room", roomItem)
                        putExtra("to", to)
                        putExtra("intent_type", TypeCamera.ATTACHMENT.name)
                    }
                }
                "gallery" -> {
                    intentTo(GalleryActivity::class.java) {
                        putExtra("room", roomItem)
                        putExtra("to", to)
                        putExtra("intent_type", TypeCamera.ATTACHMENT.name)
                    }
                }
                "location" -> {
                    // sharing location activity
                }

            }
        }
    }

    private fun isAttachIsShown() = card_attach.visibility == View.VISIBLE

    private fun setupInputText(roomItem: RowRoom.RoomItem, to: String?) {
        in_chat_message.debounce(500) {
            if (isAttachIsShown()) showAttachLayout(false)

            setupTyping(it, roomItem, to)
            if (it.isNotEmpty()) {
                btn_photo.goneAnimation()
                btn_attach.goneAnimation()
            } else {
                btn_photo.visibleAnimation(0.6f)
                btn_attach.visibleAnimation(0.6f)
            }
        }

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

    private fun setupTyping(it: String, roomItem: RowRoom.RoomItem, to: String?) {
        CoroutineScope(Dispatchers.IO).launch {
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

            network.send(messageBody, object : NetworkMessage.MessageCallback {
                override fun onSuccess() {
                }

                override fun onFailed(message: String?) {
                }
            })


            val isUrl = UrlUtil.isUrl(it)
            if (isUrl) {
                val urlValid = UrlUtil.extractUrl(it)
                val url = UrlFetcher.getThumbnail(urlValid)
                if (url != null) {
                    runOnUiThread {
                        showCardUrl(isUrl)
                        mUrlAttachment = urlAttachment {
                            this.image = url.image
                            this.title = url.title
                            this.subtitle = url.subtitle
                            this.url = url.url
                        }
                        setupViewCardUrl()
                    }
                }
            }
        }
    }

    private fun setupViewCardUrl() {
        Picasso.get().load(mUrlAttachment?.image).resize(300, 0).into(img_url_input)
        tx_title_url_input.text = mUrlAttachment?.title
        tx_subtitle_url_input.text = mUrlAttachment?.subtitle
        tx_url_input.text = mUrlAttachment?.url

        if (mUrlAttachment?.image == null) img_url_input.visibility = View.GONE
    }

    private fun showAttachLayout(show: Boolean) {
        if (show) {
            card_attach.animate()
                .alpha(1f)
                .translationY(1f)
                .withStartAction {
                    card_attach.visibility = View.VISIBLE
                }
                .setDuration(200)
                .start()
        } else {
            card_attach.animate()
                .alpha(0f)
                .translationY(100f)
                .withEndAction {
                    card_attach.visibility = View.GONE
                }
                .setDuration(200)
                .start()
        }
    }

    private fun showCardUrl(show: Boolean) {
        if (show) {
            card_url_input.animate()
                .alpha(1f)
                .translationY(1f)
                .withStartAction {
                    card_url_input.visibility = View.VISIBLE
                }
                .withEndAction {
                    normallyRecyclerview()
                }
                .setDuration(200)
                .start()
        } else {
            card_url_input.animate()
                .alpha(0f)
                .translationY(100f)
                .withEndAction {
                    card_url_input.visibility = View.GONE
                    normallyRecyclerview()
                }
                .setDuration(200)
                .start()
        }
    }

    private fun sendMessage(roomItem: RowRoom.RoomItem, to: String?) {
        val messageString = in_chat_message.text.toString()

        roomItem.subtitleRoom = messageString
        roomItem.lastDate = System.currentTimeMillis()
        roomItem.localChatStatus = LocalChatStatus.SEND
        roomItem.imageBadge = false

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

                if (mUrlAttachment != null) {
                    this.urlAttachment = mUrlAttachment
                }
            }

            roomViewModel.insert(roomItem.toLocalRoom())
            chatRoomViewModel.insert(chat.toLocalChat())
            mUrlAttachment = null

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
                showCardUrl(false)
            }
        }
    }

    private fun setupRecyclerViewScrollListener() {
        rv_chat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isReachBottom = !rv_chat.canScrollVertically(1)
            }
        })
    }

    override fun onKeyboardVisibilityChanged(keyboardVisible: Boolean) {
        normallyRecyclerview()
    }

    private fun normallyRecyclerview() {
        if (chatSize > 1) {
            if (isReachBottom) rv_chat.scrollToPosition(chatSize - 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        enableNotificationInRoom()
    }
}