package com.dome.note.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dome.note.dao.NoteDao
import com.dome.note.entity.NoteEntity

/**
 * Created by dome
 */

@Database(entities = [NoteEntity::class], version = 1)

abstract class AppDatabase : RoomDatabase(){
    companion object {
        fun getAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "db_app").build()
    }

    abstract fun noteDao(): NoteDao
}