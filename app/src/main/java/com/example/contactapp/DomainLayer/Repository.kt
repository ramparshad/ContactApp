package com.example.contactapp.DomainLayer

import com.example.contactapp.DataLayer.Contact
import com.example.contactapp.DataLayer.ContactDao
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ContactRepository @Inject constructor(
    private val contactDao: ContactDao
) {
    suspend fun upsertContact(contact: Contact) = contactDao.upsertContact(contact)
    suspend fun deleteContact(contact: Contact) = contactDao.deleteContact(contact)
    fun getAllContacts(): Flow<List<Contact>> = contactDao.getAllContacts()
    suspend fun getContactById(id: Int): Contact? = contactDao.getContactById(id)
}
