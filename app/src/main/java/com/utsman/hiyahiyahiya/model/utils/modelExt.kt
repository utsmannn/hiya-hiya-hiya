package com.utsman.hiyahiyahiya.model.utils

import com.utsman.hiyahiyahiya.data.UnreadPref
import com.utsman.hiyahiyahiya.data.repository.ContactRepository
import com.utsman.hiyahiyahiya.data.repository.StoryRepository
import com.utsman.hiyahiyahiya.database.entity.LocalChat
import com.utsman.hiyahiyahiya.database.entity.LocalRoom
import com.utsman.hiyahiyahiya.database.entity.LocalStory
import com.utsman.hiyahiyahiya.database.entity.LocalUser
import com.utsman.hiyahiyahiya.model.features.ImageBB
import com.utsman.hiyahiyahiya.model.features.ImageBBSimple
import com.utsman.hiyahiyahiya.model.row.RowChatItem
import com.utsman.hiyahiyahiya.model.row.RowContact
import com.utsman.hiyahiyahiya.model.row.RowRoom
import com.utsman.hiyahiyahiya.model.row.RowStory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun LocalUser.toContact() = RowContact.Contact(id, name, photoUri, about)
fun RowContact.Contact.toLocalUser() = LocalUser(id, name, photoUrl, about)

fun LocalRoom.toRowRoom(): RowRoom.RoomItem {
    val unreadCount = UnreadPref.getUnreadCount(id)
    return RowRoom.RoomItem(id, chatsId, membersId, lastDate, titleRoom, subtitleRoom, imageRoom, localChatStatus, imageBadge, unreadCount)
}

fun RowRoom.RoomItem.toLocalRoom() = LocalRoom(id, chatsId, membersId, lastDate, titleRoom, subtitleRoom, imageRoom, localChatStatus, imageBadge)

fun LocalChat.toChat() = RowChatItem.ChatItem(id, message, to, from, time, roomId, imageAttachment, urlAttachment, currentUser, localChatStatus)
fun RowChatItem.ChatItem.toLocalChat() = LocalChat(id, message, to, from, time, roomId, imageAttachment, urlAttachment, currentUser, localChatStatus)

fun ImageBB.toImageBBSimple(): ImageBBSimple {
    val thumb = data?.thumb?.url
    val url = data?.medium?.url
    val urlLarge = data?.image?.url
    return ImageBBSimple(thumb, url, urlLarge)
}

fun LocalStory.toStory(contactRepository: ContactRepository, storyRepository: StoryRepository): RowStory.StoryItem {
    val userName = contactRepository.localUser(userId)?.name ?: "Unknown"
    val imageBBs = storyRepository.localImageBBList()
        .filter { localImageBBIds.contains(it.id) }
        .map { it.imageBBSimple }

    return RowStory.StoryItem(
        id, userName, time, imageBbs = imageBBs
    )
}