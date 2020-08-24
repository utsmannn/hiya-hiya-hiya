package com.utsman.hiyahiyahiya.model

import com.utsman.hiyahiyahiya.database.entity.LocalChat
import com.utsman.hiyahiyahiya.database.entity.LocalRoom
import com.utsman.hiyahiyahiya.database.entity.LocalUser

fun LocalUser.toContact() = RowContact.Contact(id, name, photoUri, about)
fun RowContact.Contact.toLocalUser() = LocalUser(id, name, photoUrl, about)

fun LocalRoom.toRowRoom() = RowRoom.RoomItem(id, chatsId, membersId, lastDate, titleRoom, subtitleRoom, imageRoom, localChatStatus)
fun RowRoom.RoomItem.toLocalRoom() = LocalRoom(id, chatsId, membersId, lastDate, titleRoom, subtitleRoom, imageRoom, localChatStatus)

fun LocalChat.toChat() = RowChatItem.ChatItem(id, message, to, from, time, roomId, imageAttachment, urlAttachment, currentUser, localChatStatus)
fun RowChatItem.ChatItem.toLocalChat() = LocalChat(id, message, to, from, time, roomId, imageAttachment, urlAttachment, currentUser, localChatStatus)