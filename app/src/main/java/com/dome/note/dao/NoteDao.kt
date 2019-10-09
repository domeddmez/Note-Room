package com.dome.note.dao

import androidx.room.*
import com.dome.note.entity.NoteEntity

/**
 * Created by dome
 */

@Dao
interface NoteDao {
    @Insert
    fun insertNote(noteEntity: NoteEntity)

    @Delete
    fun deleteNote(noteEntity: NoteEntity)

    @Update
    fun updateNote(noteEntity: NoteEntity)

    @Query("SELECT * FROM note")
    fun getAllNote(): List<NoteEntity>

    @Query("SELECT * FROM note Where note.id = :id")
    fun getAllDetails(id: Int?): List<NoteEntity>

    @Query("DELETE FROM note")
    fun deleteTable()
}