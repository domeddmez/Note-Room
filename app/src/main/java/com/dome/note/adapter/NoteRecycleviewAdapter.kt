package com.dome.note.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dome.note.R
import com.dome.note.entity.NoteEntity
import kotlinx.android.synthetic.main.item_list_note.view.*

/**
 * Created by dome
 */
class NoteRecycleviewAdapter(private val items: List<NoteEntity>) :
    RecyclerView.Adapter<NoteRecycleviewAdapter.ViewHolder>() {

    lateinit var listener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list_note,
                parent,
                false
            )
        )

    }

    interface OnItemClickListener {
        fun onClick(id: Int?)
        fun onLongClick(id: Int?)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }


    class ViewHolder(itemsView: View) : RecyclerView.ViewHolder(itemsView) {
        fun bind(note: NoteEntity, listener: OnItemClickListener) {
            itemView.apply {
                tv_title.text = note.title
                tv_date.text = note.date
                root.setOnClickListener {
                    listener.onClick(note.id)

                }
                root.setOnLongClickListener {
                    listener.onLongClick(note.id)
                    true
                }
            }
        }
    }
}