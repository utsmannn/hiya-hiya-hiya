package com.utsman.hiyahiyahiya.data.repository

import com.utsman.hiyahiyahiya.database.LocalUserDatabase
import com.utsman.hiyahiyahiya.database.entity.LocalUser

class ContactRepository(private val localUserDatabase: LocalUserDatabase) {

    fun localUsers() = localUserDatabase.localUserDao().localUsers()
    fun localUser(idString: String) = localUserDatabase.localUserDao().localUser(idString)
    suspend fun insert(localUser: LocalUser) = localUserDatabase.localUserDao().insert(localUser)
    suspend fun update(localUser: LocalUser) = localUserDatabase.localUserDao().update(localUser)
    suspend fun delete(localUser: LocalUser) = localUserDatabase.localUserDao().delete(localUser)
}