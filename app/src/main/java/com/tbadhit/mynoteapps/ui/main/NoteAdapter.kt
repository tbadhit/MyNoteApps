package com.tbadhit.mynoteapps.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tbadhit.mynoteapps.R
import com.tbadhit.mynoteapps.database.Note
import com.tbadhit.mynoteapps.databinding.ItemNoteBinding
import com.tbadhit.mynoteapps.helper.NoteDiffCallback
import com.tbadhit.mynoteapps.ui.insert.NoteAddUpdateActivity

class NoteAdapter (private val activity: AppCompatActivity): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val listNotes = ArrayList<Note>()
    private lateinit var binding: ItemNoteBinding

    fun setListNote(listNotes: List<Note>) {
        val diffCallback = NoteDiffCallback(this.listNotes, listNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.listNotes.clear()
        this.listNotes.addAll(listNotes)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

    override fun getItemCount(): Int = listNotes.size

    private val resultLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if (result.data != null) {
            when(result.resultCode) {
                NoteAddUpdateActivity.RESULT_UPDATE -> {
                    showSnackBarMessage(activity.getString(R.string.changed))
                }
                NoteAddUpdateActivity.RESULT_DELETE -> {
                    showSnackBarMessage(activity.getString(R.string.deleted))
                }
            }
        }
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.root as View, message, Snackbar.LENGTH_SHORT).show()
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            with(binding) {
                tvItemTitle.text = note.title
                tvItemDate.text = note.date
                tvItemDescription.text = note.description
                cvItemNote.setOnClickListener {
                    val intent = Intent(activity, NoteAddUpdateActivity::class.java)
                    intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, adapterPosition)
                    intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, note)
                    resultLauncher.launch(intent)
                }
            }
        }
    }

}