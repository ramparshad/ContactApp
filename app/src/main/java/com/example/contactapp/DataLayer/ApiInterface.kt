package com.example.contactapp.DataLayer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    // insert and update contacts
    @Upsert
    suspend fun upsertContact(contact: Contact)

    // for deleting contacts
    @Delete
    suspend fun deleteContact(contact: Contact)

    // for fetch all contacts initially
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<Contact>>

    // for editing
    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Int): Contact?

    // for getting all deleted contacts
    @Query("SELECT * FROM contacts WHERE isDeleted = 1 ORDER BY name ASC")
    fun getDeletedContacts(): Flow<List<Contact>>

}


