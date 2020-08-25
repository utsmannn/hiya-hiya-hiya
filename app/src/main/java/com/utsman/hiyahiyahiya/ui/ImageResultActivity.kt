package com.utsman.hiyahiyahiya.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.data.UserPref
import com.utsman.hiyahiyahiya.database.LocalUserDatabase
import com.utsman.hiyahiyahiya.di.network
import com.utsman.hiyahiyahiya.di.networkImage
import com.utsman.hiyahiyahiya.model.features.ImageAttachment
import com.utsman.hiyahiyahiya.model.features.ImageBBSimple
import com.utsman.hiyahiyahiya.model.row.RowRoom
import com.utsman.hiyahiyahiya.model.types.LocalChatStatus
import com.utsman.hiyahiyahiya.model.types.TypeMessage
import com.utsman.hiyahiyahiya.model.utils.*
import com.utsman.hiyahiyahiya.network.NetworkImageBB
import com.utsman.hiyahiyahiya.network.NetworkMessage
import com.utsman.hiyahiyahiya.ui.viewmodel.ChatViewModel
import com.utsman.hiyahiyahiya.ui.viewmodel.RoomViewModel
import com.utsman.hiyahiyahiya.utils.*
import kotlinx.android.synthetic.main.activity_image_result.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*

class ImageResultActivity : AppCompatActivity() {

    private val imagePath by lazy { intent.getStringExtra("image_path") }
    private val roomItem by lazy { intent.getParcelableExtra<RowRoom.RoomItem>("room") }
    private val toMember by lazy { intent.getStringExtra("to") }

    private val localUserDb: LocalUserDatabase by inject()
    private val chatRoomViewModel: ChatViewModel by viewModel()
    private val roomViewModel: RoomViewModel by viewModel()
    private val network: NetworkMessage by network()
    private val networkImage: NetworkImageBB by networkImage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_result)
        progress_circular.visibility = View.GONE

        val file = File(imagePath)
        Picasso.get().load(file).into(img_result)

        btn_send_message.click {
            progress_circular.visibility = View.VISIBLE
            val bytes = file.readBytes()

            if (roomItem == null) {
                btn_send_message.visibility = View.GONE
            } else {
                btn_send_message.visibility = View.VISIBLE
            }

            roomItem?.let { room ->
                val messageString = in_chat_message.text.toString()
                room.subtitleRoom = messageString
                room.localChatStatus = LocalChatStatus.SEND

                GlobalScope.launch {
                    networkImage.uploadImage(this@ImageResultActivity, bytes.toBase64(), object : NetworkImageBB.ImageBBCallback {
                        override fun onSuccess(imageBB: ImageBBSimple) {
                            val imageAttachment = imageAttachment {
                                this.imageBBSimple = imageBB
                            }
                            GlobalScope.launch {
                                sendMessage(messageString, room, imageAttachment)
                            }
                        }

                        override fun onFailed(message: String?) {
                            logi(message)
                            progress_circular.visibility = View.GONE
                        }

                    })
                }
            }
        }
    }

    private suspend fun sendMessage(messageString: String, room: RowRoom.RoomItem, imageAttachment: ImageAttachment) {
        val currentUser = localUserDb.localUserDao().localUser(UserPref.getUserId())

        val chat = chatItem {
            this.id = UUID.randomUUID().toString()
            this.from = UserPref.getUserId()
            this.message = messageString
            this.roomId = room.id
            this.time = System.currentTimeMillis()
            this.to = toMember
            this.currentUser = currentUser
            this.imageAttachment = listOf(imageAttachment)
        }

        val messageBody = messageBody {
            this.fromMessage = chat.from
            this.toMessage = chat.to
            this.typeMessage = TypeMessage.MESSAGE
            this.payload = chat.toLocalChat()
        }

        chatRoomViewModel.insert(chat.toLocalChat())
        roomViewModel.insert(room.toLocalRoom())

        network.send(this, messageBody, object : NetworkMessage.MessageCallback {
            override fun onSuccess() {
                progress_circular.visibility = View.GONE
                finish()
                logi("success")
            }

            override fun onFailed(message: String?) {
                progress_circular.visibility = View.GONE
                toast("failed")
                logi("failed ------> $message")
            }
        })

        runOnUiThread {
            in_chat_message.setText("")
        }
    }
}