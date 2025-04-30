package com.example.contactapp.PresentationLayer.View

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactapp.DataLayer.Contact
import com.example.contactapp.DomainLayer.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllContacts().collect { contactList ->
                _contacts.value = contactList
            }
        }
    }
    // why we are using upsertContact instead of insertContact
    // because we want to insert a new contact or update the existing contact

    fun upsertContact(contact: Contact) {
        viewModelScope.launch {
            repository.upsertContact(contact)
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }

    suspend fun getContactById(id: Int): Contact? {
        return repository.getContactById(id)
    }
}


