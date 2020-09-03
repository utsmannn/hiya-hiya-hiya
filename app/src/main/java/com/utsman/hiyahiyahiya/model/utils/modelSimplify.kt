package com.utsman.hiyahiyahiya.model.utils

import com.utsman.hiyahiyahiya.database.entity.LocalImageBB
import com.utsman.hiyahiyahiya.database.entity.LocalStory
import com.utsman.hiyahiyahiya.database.entity.LocalUser
import com.utsman.hiyahiyahiya.model.body.*
import com.utsman.hiyahiyahiya.model.features.*
import com.utsman.hiyahiyahiya.model.row.*

fun messageBody(body: MessageBody.() -> Unit) = MessageBody().apply(body)
fun messageStatusBody(body: MessageStatusBody.() -> Unit) = MessageStatusBody().apply(body)
fun typingBody(body: TypingBody.() -> Unit) = TypingBody().apply(body)
fun storyBody(body: StoryBody.() -> Unit) = StoryBody().apply(body)
fun readBody(body: ReadBody.() -> Unit) = ReadBody().apply(body)

fun localUser(u: LocalUser.() -> Unit) = LocalUser().apply(u)
fun localStory(story: LocalStory.() -> Unit) = LocalStory().apply(story)
fun localImageBB(imageBB: LocalImageBB.() -> Unit) = LocalImageBB().apply(imageBB)

fun chatRoom(roomItem: RowRoom.RoomItem.() -> Unit) = RowRoom.RoomItem().apply(roomItem)
fun chatItem(item: RowChatItem.ChatItem.() -> Unit) = RowChatItem.ChatItem().apply(item)
fun photo(photoLocal: PhotoLocal.() -> Unit) = PhotoLocal().apply(photoLocal)
fun story(story: RowStory.StoryItem.() -> Unit) = RowStory.StoryItem().apply(story)

fun imageAttachment(attachment: ImageAttachment.() -> Unit) = ImageAttachment().apply(attachment)
fun urlAttachment(attachment: UrlAttachment.() -> Unit) = UrlAttachment().apply(attachment)
fun rowAttachment(rowAttachChat: RowAttachChat.() -> Unit) = RowAttachChat().apply(rowAttachChat)

fun rowSharedLocation(rowSharedLocation: RowSharedLocation.() -> Unit) = RowSharedLocation().apply(rowSharedLocation)