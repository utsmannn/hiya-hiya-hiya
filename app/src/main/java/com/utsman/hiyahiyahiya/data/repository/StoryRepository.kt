package com.utsman.hiyahiyahiya.data.repository

import com.utsman.hiyahiyahiya.database.LocalImageBBDatabase
import com.utsman.hiyahiyahiya.database.LocalStoryDatabase
import com.utsman.hiyahiyahiya.database.entity.LocalImageBB
import com.utsman.hiyahiyahiya.database.entity.LocalStory

class StoryRepository(private val storyDatabase: LocalStoryDatabase, private val localImageBBDatabase: LocalImageBBDatabase) {

    fun localStories() = storyDatabase.localStoryDao().localStories()
    fun localStory(id: String) = storyDatabase.localStoryDao().localStory(id)
    suspend fun insert(localStory: LocalStory) = storyDatabase.localStoryDao().insert(localStory)
    suspend fun update(localStory: LocalStory) = storyDatabase.localStoryDao().update(localStory)
    suspend fun delete(localStory: LocalStory) = storyDatabase.localStoryDao().delete(localStory)

    fun localImageBBs() = localImageBBDatabase.localImageBBDao().localImageBBs()
    fun localImageBBList() = localImageBBDatabase.localImageBBDao().localImageBBList()
    fun localImageBB(id: String) = localImageBBDatabase.localImageBBDao().localImageBB(id)
    suspend fun insertImageBB(localImageBB: LocalImageBB) = localImageBBDatabase.localImageBBDao().insert(localImageBB)
    suspend fun updateImageBB(localImageBB: LocalImageBB) = localImageBBDatabase.localImageBBDao().update(localImageBB)
    suspend fun deleteImageBB(localImageBB: LocalImageBB) = localImageBBDatabase.localImageBBDao().delete(localImageBB)
}