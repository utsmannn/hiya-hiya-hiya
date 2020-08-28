package com.utsman.hiyahiyahiya.ui.activity

import android.app.Activity
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
import com.utsman.hiyahiyahiya.model.types.TypeCamera
import com.utsman.hiyahiyahiya.model.types.TypeMessage
import com.utsman.hiyahiyahiya.model.utils.*
import com.utsman.hiyahiyahiya.network.NetworkImageBB
import com.utsman.hiyahiyahiya.network.NetworkMessage
import com.utsman.hiyahiyahiya.ui.viewmodel.ChatViewModel
import com.utsman.hiyahiyahiya.ui.viewmodel.RoomViewModel
import com.utsman.hiyahiyahiya.ui.viewmodel.StoryViewModel
import com.utsman.hiyahiyahiya.utils.*
import kotlinx.android.synthetic.main.activity_image_result.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*

class CameraResultActivity : AppCompatActivity() {

    private val imagePath by lazy { intent.getStringExtra("image_path") }
    private val roomItem by lazy { intent.getParcelableExtra<RowRoom.RoomItem>("room") }
    private val toMember by lazy { intent.getStringExtra("to") }
    private val intentType by lazy { intent.getStringExtra("intent_type") }

    private val localUserDb: LocalUserDatabase by inject()
    private val chatRoomViewModel: ChatViewModel by viewModel()
    private val roomViewModel: RoomViewModel by viewModel()
    private val storyViewModel: StoryViewModel by viewModel()

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
            container_input.alpha = 0.3f

            val bytes = file.readBytes()

            GlobalScope.launch {
                networkImage.uploadImage(this@CameraResultActivity, bytes.toBase64(), object : NetworkImageBB.ImageBBCallback {
                    override fun onSuccess(imageBB: ImageBBSimple) {
                        GlobalScope.launch {
                            when (intentType) {
                                TypeCamera.ATTACHMENT.name -> {
                                    val imageAttachment = imageAttachment {
                                        this.imageBBSimple = imageBB
                                    }

                                    if (roomItem != null) {
                                        val messageString = in_chat_message.text.toString()
                                        roomItem?.subtitleRoom = messageString
                                        roomItem?.localChatStatus = LocalChatStatus.SEND
                                        sendMessage(messageString, roomItem, imageAttachment)
                                    } else {
                                        runOnUiThread {
                                            progress_circular.visibility = View.VISIBLE
                                            toast("failed")
                                        }
                                    }

                                }
                                TypeCamera.STORY.name -> {
                                    sendStory(imageBB)
                                }
                            }
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

    private suspend fun sendStory(imageBB: ImageBBSimple) {
        val storyBody = storyBody {
            this.userId = UserPref.getUserId()
            this.time = System.currentTimeMillis()
            this.imageBB = imageBB
        }

        val messageBody = messageBody {
            this.fromMessage = UserPref.getUserId()
            this.typeMessage = TypeMessage.STORY
            this.payload = storyBody
        }

        val storyId = generateIdStory(UserPref.getUserId())
        val localImageBBId = generateIdImageBB(UserPref.getUserId())
        val storyExist = storyViewModel.story(storyId)
        val localImageBBIds = (storyExist?.localImageBBIds ?: emptyList()).toMutableList()
        localImageBBIds.add(localImageBBId)

        val localStory = localStory {
            this.id = generateIdStory(UserPref.getUserId())
            this.time = System.currentTimeMillis()
            this.userId = UserPref.getUserId()
            this.localImageBBIds = localImageBBIds
        }

        val localImageBB = localImageBB {
            this.id = localImageBBId
            this.imageBBSimple = imageBB
            this.time = System.currentTimeMillis()
        }

        storyViewModel.insertImageBB(localImageBB)
        storyViewModel.insert(localStory)

        network.send(this, messageBody, object : NetworkMessage.MessageCallback {
            override fun onSuccess() {
                logi("activity finish")
                progress_circular.visibility = View.GONE
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onFailed(message: String?) {
                progress_circular.visibility = View.GONE
                toast("failed")
                logi("failed ------> $message")
            }

        })
    }

    private suspend fun sendMessage(messageString: String, room: RowRoom.RoomItem, imageAttachment: ImageAttachment) {
        val currentUser = localUserDb.localUserDao().localUser(UserPref.getUserId())

        room.lastDate = System.currentTimeMillis()
        room.imageBadge = true

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
                in_chat_message.setText("")
                progress_circular.visibility = View.GONE
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onFailed(message: String?) {
                progress_circular.visibility = View.GONE
                toast("failed")
                logi("failed ------> $message")
            }
        })
    }
}