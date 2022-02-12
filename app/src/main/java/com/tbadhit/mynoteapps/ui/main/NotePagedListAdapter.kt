package com.tbadhit.mynoteapps.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tbadhit.mynoteapps.database.Note
import com.tbadhit.mynoteapps.databinding.ItemNoteBinding
import com.tbadhit.mynoteapps.ui.insert.NoteAddUpdateActivity

class NotePagedListAdapter(private val activity: AppCompatActivity): PagingDataAdapter<Note, NotePagedListAdapter.NoteViewHolder>(
    DIFF_CALLBACK) {

    private val resultLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.data != null) {
            when(result.resultCode) {
                NoteAddUpdateActivity.RESULT_UPDATE -> {
//                    showSnackBarMessage(activity.getString(R.string.changed))
                }
                NoteAddUpdateActivity.RESULT_DELETE -> {
//                    showSnackBarMessage(activity.getString(R.string.deleted))
                }
            }
        }
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            with(binding) {
                tvItemTitle.text = note.title
                tvItemDate.text = note.date
                tvItemDescription.text = note.description
                cvItemNote.setOnClickListener {
                    val intent = Intent(activity, NoteAddUpdateActivity::class.java)
                    intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, bindingAdapterPosition)
                    intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, note)
                    resultLauncher.launch(intent)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position) as Note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Note> = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.title == newItem.title && oldItem.description == newItem.description
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }

        }
    }


}