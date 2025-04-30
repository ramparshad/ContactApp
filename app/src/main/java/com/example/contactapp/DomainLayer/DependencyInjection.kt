package com.example.contactapp.DomainLayer


import android.content.Context
import androidx.room.Room
import com.example.contactapp.DataLayer.ContactDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ContactDatabase =
        Room.databaseBuilder(context, ContactDatabase::class.java, "contact_db")
            .build()

    @Provides
    @Singleton
    fun provideContactDao(database: ContactDatabase) = database.contactDao()
}