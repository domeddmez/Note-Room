package com.dome.note

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dome.note.database.AppDatabase
import com.dome.note.entity.NoteEntity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_note.*
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        val appDatabase = AppDatabase.getAppDatabase(this)

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        tv_date.text = currentDate


        bt_save.setOnClickListener {
            val title: String = et_title.text.toString()
            val details: String = et_details.text.toString()
            val date: String = tv_date.text.toString()

            if (title.isEmpty()) {
                et_title.error = "Title is Empty"
            }
            if (details.isEmpty()) {
                et_details.error = "Details is Empty"
            } else {
                val noteEntity = NoteEntity()
                noteEntity.title = title
                noteEntity.details = details
                noteEntity.date = date

                Flowable.fromCallable { appDatabase.noteDao().insertNote(noteEntity) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Toast.makeText(this, "Save Success", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()

                    }
            }

        }
    }
}
