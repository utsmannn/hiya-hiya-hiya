package com.utsman.hiyahiyahiya.model

import com.utsman.hiyahiyahiya.database.entity.LocalChat
import com.utsman.hiyahiyahiya.database.entity.LocalRoom
import com.utsman.hiyahiyahiya.database.entity.LocalUser

fun LocalUser.toContact() = RowContact.Contact(id, name, photoUri, about)
fun RowContact.Contact.toLocalUser() = LocalUser(id, name, photoUrl, about)

fun LocalRoom.toChatRoom() = RowChat.ChatRoom(id, chatsId, membersId, lastDate)
fun RowChat.ChatRoom.toLocalRoom() = LocalRoom(id, chatsId, membersId, lastDate)

fun LocalChat.toChat() = RowChatItem.ChatItem(id, message, to, from, time, roomId, attachment)
fun RowChatItem.ChatItem.toLocalChat() = LocalChat(id, message, to, from, time, roomId, attachment)