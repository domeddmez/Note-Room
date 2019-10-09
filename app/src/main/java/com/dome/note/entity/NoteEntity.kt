package com.dome.note.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by dome
 */

@Entity(tableName = "note")
data class NoteEntity(@PrimaryKey(autoGenerate = true) var id: Int? = null,
                      var title: String? = null,
                      var details: String? = null,
                      var date: String? = null)