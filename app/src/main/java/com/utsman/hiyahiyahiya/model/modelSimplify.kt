package com.utsman.hiyahiyahiya.model

import com.utsman.hiyahiyahiya.database.entity.LocalUser

fun localUser(u: LocalUser.() -> Unit) = LocalUser().apply(u)
fun messageBody(msg: MessageBody.() -> Unit) = MessageBody().apply(msg)
fun messageStatusBody(body: MessageStatusBody.() -> Unit) = MessageStatusBody().apply(body)
fun chatRoom(roomItem: RowRoom.RoomItem.() -> Unit) = RowRoom.RoomItem().apply(roomItem)
fun chatItem(item: RowChatItem.ChatItem.() -> Unit) = RowChatItem.ChatItem().apply(item)
