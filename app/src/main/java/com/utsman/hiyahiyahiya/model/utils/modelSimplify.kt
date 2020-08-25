package com.utsman.hiyahiyahiya.model.utils

import com.utsman.hiyahiyahiya.database.entity.LocalUser
import com.utsman.hiyahiyahiya.model.features.*
import com.utsman.hiyahiyahiya.model.row.RowChatItem
import com.utsman.hiyahiyahiya.model.row.RowRoom

fun messageBody(msg: MessageBody.() -> Unit) = MessageBody().apply(msg)
fun messageStatusBody(body: MessageStatusBody.() -> Unit) = MessageStatusBody().apply(body)
fun typingBody(typingBody: TypingBody.() -> Unit) = TypingBody().apply(typingBody)

fun localUser(u: LocalUser.() -> Unit) = LocalUser().apply(u)
fun chatRoom(roomItem: RowRoom.RoomItem.() -> Unit) = RowRoom.RoomItem().apply(roomItem)
fun chatItem(item: RowChatItem.ChatItem.() -> Unit) = RowChatItem.ChatItem().apply(item)
fun photo(photoLocal: PhotoLocal.() -> Unit) = PhotoLocal().apply(photoLocal)

fun imageAttachment(attachment: ImageAttachment.() -> Unit) = ImageAttachment().apply(attachment)
fun urlAttachment(attachment: UrlAttachment.() -> Unit) = UrlAttachment().apply(attachment)