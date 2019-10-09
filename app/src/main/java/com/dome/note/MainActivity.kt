package com.dome.note

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.dome.note.adapter.NoteRecycleviewAdapter
import com.dome.note.database.AppDatabase
import com.dome.note.entity.NoteEntity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var appDatabase: AppDatabase

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appDatabase = AppDatabase.getAppDatabase(this)

        fab.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
            finish()
        }

        getData(appDatabase)
    }

    @SuppressLint("CheckResult")
    private fun getData(appDatabase: AppDatabase) {
        Flowable.fromCallable { appDatabase.noteDao().getAllNote() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val noteAdapter = NoteRecycleviewAdapter(it)
                rv_note.apply {
                    layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                    isNestedScrollingEnabled = false
                    adapter = noteAdapter
                    noteAdapter.setOnItemClickListener(object :
                        NoteRecycleviewAdapter.OnItemClickListener {
                        override fun onClick(id: Int?) {
                            val i = Intent(this@MainActivity, DetailsActivity::class.java)
                            i.putExtra("id", id)
                            startActivity(i)
                            finish()
                        }

                        override fun onLongClick(id: Int?) {
                            dialogDelete(id, appDatabase)
                        }
                    })

                }
            }
    }

    private fun dialogDelete(id: Int?, appDatabase: AppDatabase) {
        val alertDialog: AlertDialog.Builder =
            AlertDialog.Builder(this)
        alertDialog.setCancelable(false)
        alertDialog.setTitle("Delete ?")
        alertDialog.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            val noteEntity = NoteEntity()
            noteEntity.id = id

            Flowable.fromCallable { appDatabase.noteDao().deleteNote(noteEntity) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Toast.makeText(this, "Delete Success", Toast.LENGTH_SHORT).show()
                    getData(appDatabase)
                }
        }

        alertDialog.setNegativeButton("CanCel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: Dialog = alertDialog.create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.ic_delete -> {
            Flowable.fromCallable { appDatabase.noteDao().deleteTable() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Toast.makeText(this, "Delete All", Toast.LENGTH_SHORT).show()
                    getData(appDatabase)
                }
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
