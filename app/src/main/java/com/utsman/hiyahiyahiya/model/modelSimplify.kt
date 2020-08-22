package com.utsman.hiyahiyahiya.model

import com.utsman.hiyahiyahiya.database.entity.LocalUser

fun localUser(u: LocalUser.() -> Unit) = LocalUser().apply(u)
fun messageBody(msg: MessageBody.() -> Unit) = MessageBody().apply(msg)
fun chatRoom(room: RowChat.ChatRoom.() -> Unit) = RowChat.ChatRoom().apply(room)
fun chatItem(item: RowChatItem.ChatItem.() -> Unit) = RowChatItem.ChatItem().apply(item)