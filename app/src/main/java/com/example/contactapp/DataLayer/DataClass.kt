package com.example.contactapp.DataLayer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,

)
