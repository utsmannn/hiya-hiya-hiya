package com.utsman.hiyahiyahiya.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.utsman.hiyahiyahiya.data.repository.ContactRepository
import com.utsman.hiyahiyahiya.model.toContact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ContactViewModel(private val contactRepository: ContactRepository) : ViewModel() {
    val contacts = contactRepository.localUsers()
        .map { it.map { u -> u.toContact() } }
        .flowOn(Dispatchers.Main)
        .asLiveData(viewModelScope.coroutineContext)
}