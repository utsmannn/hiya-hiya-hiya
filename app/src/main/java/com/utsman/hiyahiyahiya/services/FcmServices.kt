package com.utsman.hiyahiyahiya.services

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.data.UserPref
import com.utsman.hiyahiyahiya.database.*
import com.utsman.hiyahiyahiya.database.entity.LocalChat
import com.utsman.hiyahiyahiya.database.entity.LocalRoom
import com.utsman.hiyahiyahiya.database.entity.LocalUser
import com.utsman.hiyahiyahiya.di.network
import com.utsman.hiyahiyahiya.network.NetworkMessage
import com.utsman.hiyahiyahiya.model.types.TypeMessage
import com.utsman.hiyahiyahiya.model.body.MessageStatusBody
import com.utsman.hiyahiyahiya.model.body.StoryBody
import com.utsman.hiyahiyahiya.model.body.TypingBody
import com.utsman.hiyahiyahiya.model.features.ImageAttachment
import com.utsman.hiyahiyahiya.model.types.LocalChatStatus
import com.utsman.hiyahiyahiya.model.utils.*
import com.utsman.hiyahiyahiya.ui.ChatRoomActivity
import com.utsman.hiyahiyahiya.utils.Broadcast
import com.utsman.hiyahiyahiya.utils.generateIdImageBB
import com.utsman.hiyahiyahiya.utils.generateIdStory
import com.utsman.hiyahiyahiya.utils.logi
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.lang.Exception
import java.util.*

class FcmServices : FirebaseMessagingService() {
    private val gson = Gson()
    private val localUserDb: LocalUserDatabase by inject()
    private val localChatDb: LocalChatDatabase by inject()
    private val localRoomDb: LocalRoomDatabase by inject()
    private val localStoryDatabase: LocalStoryDatabase by inject()
    private val localImageBBDatabase: LocalImageBBDatabase by inject()

    private val network: NetworkMessage by network()
    private var notificationActive = true

    private lateinit var builder: NotificationCompat.Builder

    override fun onCreate() {
        super.onCreate()
        builder = NotificationCompat.Builder(this, "hiya-hiya")
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
    }

    override fun onMessageReceived(remote: RemoteMessage) {
        super.onMessageReceived(remote)
        logi("message from arrived -> ${remote.from}")
        logi("message arrived -> ${remote.data}")

        val type = remote.data["type_message"] ?: TypeMessage.DEVICE_REGISTER.name
        val payloadString = remote.data["payload"]
        when (TypeMessage.valueOf(type)) {
            TypeMessage.DEVICE_REGISTER -> {
                val payloadUser = gson.fromJson(payloadString, LocalUser::class.java)
                GlobalScope.launch {
                    localUserDb.localUserDao().insert(payloadUser)
                }
            }
            TypeMessage.MESSAGE -> {
                val payloadChat = gson.fromJson(payloadString, LocalChat::class.java)
                GlobalScope.launch {
                    localChatDb.localChatDao().insert(payloadChat)
                    payloadChat.currentUser?.let { u -> localUserDb.localUserDao().insert(u) }

                    val meId = UserPref.getUserId()
                    val otherId = payloadChat.from ?: ""

                    val roomId = payloadChat.roomId ?: UUID.randomUUID().toString()
                    val roomFound = localRoomDb.localRoomDao().localRoom(roomId)

                    val imageAttach = payloadChat.imageAttachment
                    val room = roomFound?.apply {
                        this.chatsId.toMutableList().add(payloadChat.id)
                        this.subtitleRoom = payloadChat.message
                        this.titleRoom = payloadChat.currentUser?.name
                        this.imageRoom = payloadChat?.currentUser?.photoUri
                        this.lastDate = payloadChat.time
                        this.localChatStatus = LocalChatStatus.NONE
                        this.imageBadge = imageAttach.isNotEmpty()
                    } ?: chatRoom {
                            this.id = roomId
                            this.titleRoom = payloadChat.currentUser?.name
                            this.subtitleRoom = payloadChat.message
                            this.lastDate = payloadChat.time
                            this.membersId = listOf(meId, otherId)
                            this.chatsId = listOf(payloadChat.id)
                            this.imageRoom = payloadChat.currentUser?.photoUri
                            this.localChatStatus = LocalChatStatus.NONE
                            this.imageBadge = imageAttach.isNotEmpty()
                        }.toLocalRoom()

                    delay(300)
                    localRoomDb.localRoomDao().insert(room)

                    showNotification(payloadChat, room, imageAttach) { b ->
                        val notificationId = System.currentTimeMillis().toString().takeLast(4).toInt()
                        with(NotificationManagerCompat.from(this@FcmServices)) {
                            notify(notificationId, b.build())
                        }
                    }
                    sendCallbackStatus(payloadChat, otherId)
                }
            }
            TypeMessage.LOCAL_STATUS -> {
                val payloadStatus = gson.fromJson(payloadString, MessageStatusBody::class.java)
                val chatFound = localChatDb.localChatDao().localChat(payloadStatus.chatId)

                if (chatFound != null) {
                    chatFound.localChatStatus = payloadStatus.localStatus
                    GlobalScope.launch {
                        localChatDb.localChatDao().update(chatFound)
                        val roomFound = localRoomDb.localRoomDao().localRoom(chatFound.roomId)
                        if (roomFound != null) {
                            roomFound.localChatStatus = payloadStatus.localStatus
                            localRoomDb.localRoomDao().update(roomFound)
                        }
                    }
                }
            }
            TypeMessage.TYPING -> {
                val payloadTyping = gson.fromJson(payloadString, TypingBody::class.java)
                val broadcastKey = "typing_${payloadTyping.roomId}"
                Broadcast.with(GlobalScope).post(broadcastKey)
            }
            TypeMessage.STORY -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val payloadStory = gson.fromJson(payloadString, StoryBody::class.java)
                    val userId = payloadStory.userId
                    val localStory = localStoryDatabase.localStoryDao().localStoryUser(userId)

                    val localImageBBId = generateIdImageBB(payloadStory.userId)

                    val localImageBB = localImageBB {
                        this.id = localImageBBId
                        this.time = payloadStory.time
                        this.imageBBSimple = payloadStory.imageBB
                    }

                    logi("local image bb --> $localImageBB")
                    localImageBBDatabase.localImageBBDao().insert(localImageBB)

                    logi("user id from --> $userId")
                    logi("local story found -> $localStory")
                    if (localStory != null) {
                        localStory.localImageBBIds = localStory.localImageBBIds.toMutableList().apply { add(localImageBBId) }
                        localStoryDatabase.localStoryDao().insert(localStory)
                    } else {
                        val newLocalStory = localStory {
                            this.id = generateIdStory(userId)
                            this.time = payloadStory.time
                            this.userId = userId
                            this.localImageBBIds = listOf(localImageBBId)
                        }

                        localStoryDatabase.localStoryDao().insert(newLocalStory)
                    }

                    logi("story incoming ---> $payloadStory")
                }
            }
        }

        logi("message type is -->> $type ")
    }

    private fun showNotification(payloadChat: LocalChat?, room: LocalRoom, imageAttach: List<ImageAttachment>, result: (NotificationCompat.Builder) -> Unit) {
        val intent = Intent(this, ChatRoomActivity::class.java).run {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("room", room.toRowRoom())
            putExtra("to", payloadChat?.to)
        }

        val pendingIntent =  TaskStackBuilder.create(this).run {
            addNextIntent(intent)
            getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT)
        }

        builder.apply {
            setContentIntent(pendingIntent)
            setContentTitle(payloadChat?.currentUser?.name)
            val contentText = if (imageAttach.isNotEmpty()) {
                "\uD83D\uDCF7 ${payloadChat?.message}"
            } else {
                payloadChat?.message
            }
            setContentText(contentText)
        }
        result.invoke(builder)
    }

    private fun sendCallbackStatus(payloadChat: LocalChat, otherId: String) {
        val messageStatusBody = messageStatusBody {
            this.chatId = payloadChat.id
            this.localStatus = LocalChatStatus.RECEIVED
            this.ownerId = UserPref.getUserId()
        }

        val messageBody = messageBody {
            this.fromMessage = UserPref.getUserId()
            this.toMessage = otherId
            this.typeMessage = TypeMessage.LOCAL_STATUS
            this.payload = messageStatusBody
        }

        network.send(messageBody, object : NetworkMessage.MessageCallback {
            override fun onSuccess() {
            }

            override fun onFailed(message: String?) {
            }

        })
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        val user = localUserDb.localUserDao().localUser(UserPref.getUserId())
        user?.token = newToken
        GlobalScope.launch {
            user?.let { u ->
                localUserDb.localUserDao().update(u)
                val messageBody = messageBody {
                    fromMessage = u.id
                    typeMessage = TypeMessage.DEVICE_REGISTER
                    payload = u
                }

                network.send(messageBody, object : NetworkMessage.MessageCallback {
                    override fun onSuccess() {
                        logi("broadcast new token")
                    }

                    override fun onFailed(message: String?) {
                        logi("broadcast new token failed -> $message")
                    }

                })
            }
        }
    }
}
