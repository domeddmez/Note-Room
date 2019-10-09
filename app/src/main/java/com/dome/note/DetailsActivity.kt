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
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_details.bt_save
import kotlinx.android.synthetic.main.activity_details.et_details
import kotlinx.android.synthetic.main.activity_details.et_title
import kotlinx.android.synthetic.main.activity_details.tv_date

class DetailsActivity : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val bundle: Bundle? = intent.extras
        val id = bundle?.getInt("id")

        val appDatabase = AppDatabase.getAppDatabase(this)

        Flowable.fromCallable { appDatabase.noteDao().getAllDetails(id) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                tv_date.text = it[0].date
                et_title.setText(it[0].title.toString())
                et_details.setText(it[0].details.toString())
            }

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
                noteEntity.id = id
                noteEntity.title = title
                noteEntity.details = details
                noteEntity.date = date

                Flowable.fromCallable { appDatabase.noteDao().updateNote(noteEntity) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Toast.makeText(this, "Update Success", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()

                    }
            }
        }
    }
}