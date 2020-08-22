package com.utsman.hiyahiyahiya.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.utsman.hiyahiyahiya.data.UserPref
import com.utsman.hiyahiyahiya.database.LocalChatDatabase
import com.utsman.hiyahiyahiya.database.LocalRoomDatabase
import com.utsman.hiyahiyahiya.database.LocalUserDatabase
import com.utsman.hiyahiyahiya.database.entity.LocalChat
import com.utsman.hiyahiyahiya.database.entity.LocalUser
import com.utsman.hiyahiyahiya.model.chatRoom
import com.utsman.hiyahiyahiya.model.localUser
import com.utsman.hiyahiyahiya.model.toLocalRoom
import com.utsman.hiyahiyahiya.network.TypeMessage
import com.utsman.hiyahiyahiya.utils.logi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

class FcmServices : FirebaseMessagingService() {
    private val gson = Gson()
    private val localUserDb: LocalUserDatabase by inject()
    private val localChatDb: LocalChatDatabase by inject()
    private val localRoomDb: LocalRoomDatabase by inject()

    override fun onMessageReceived(remote: RemoteMessage) {
        super.onMessageReceived(remote)
        logi("message from arrived -> ${remote.from}")
        logi("message arrived -> ${remote.data}")

        val type = remote.data["type_message"]
        val payloadString = remote.data["payload"]
        when (type) {
            TypeMessage.DEVICE_REGISTER.name -> {
                val payloadUser = gson.fromJson(payloadString, LocalUser::class.java)
                GlobalScope.launch {
                    localUserDb.localUserDao().insert(payloadUser)
                }
            }
            TypeMessage.MESSAGE.name -> {
                val payloadChat = gson.fromJson(payloadString, LocalChat::class.java)
                GlobalScope.launch {
                    localChatDb.localChatDao().insert(payloadChat)
                    payloadChat.currentUser?.let { u -> localUserDb.localUserDao().insert(u) }

                    val meId = UserPref.getUserId()
                    val otherId = payloadChat.from ?: ""

                    val roomId = payloadChat.roomId ?: UUID.randomUUID().toString()
                    val roomFound = localRoomDb.localRoomDao().localRoom(roomId)

                    if (roomFound != null) {
                        roomFound.run {
                            this.chatsId.toMutableList().add(payloadChat.id)
                            this.subtitleRoom = payloadChat.message
                            this.titleRoom = payloadChat.currentUser?.name
                            this.imageRoom = payloadChat?.currentUser?.photoUri
                            this.lastDate = payloadChat.time
                        }
                        delay(300)
                        localRoomDb.localRoomDao().update(roomFound)
                    } else {
                        val newRoom = chatRoom {
                            this.id = roomId
                            this.titleRoom = payloadChat.currentUser?.name
                            this.subtitleRoom = payloadChat.message
                            this.lastDate = payloadChat.time
                            this.membersId = listOf(meId, otherId)
                            this.chatsId = listOf(payloadChat.id)
                            this.imageRoom = payloadChat.currentUser?.photoUri
                        }

                        localRoomDb.localRoomDao().insert(newRoom.toLocalRoom())
                    }
                }
            }
        }


        logi("message type is -->> $type ")
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
    }
}