package com.tbadhit.mynoteapps.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tbadhit.mynoteapps.R
import com.tbadhit.mynoteapps.database.Note
import com.tbadhit.mynoteapps.databinding.ActivityMainBinding
import com.tbadhit.mynoteapps.helper.ViewModelFactory
import com.tbadhit.mynoteapps.ui.insert.NoteAddUpdateActivity

class MainActivity : AppCompatActivity() {

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding

    private lateinit var adapter: NoteAdapter

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result -> if (result.data != null) {
            when(result.resultCode) {
                NoteAddUpdateActivity.RESULT_ADD -> {
                    showSnackbarMessage(getString(R.string.added))
                }
            }
    }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val mainViewModel = obtainViewModel(this@MainActivity)
        mainViewModel.getAllNotes().observe(this, noteObserver)

        adapter = NoteAdapter(this@MainActivity)

        binding?.rvNotes?.layoutManager = LinearLayoutManager(this)
        binding?.rvNotes?.setHasFixedSize(true)
        binding?.rvNotes?.adapter = adapter

        binding?.fabAdd?.setOnClickListener { view ->
            if (view.id == R.id.fab_add) {
                val intent = Intent(this@MainActivity, NoteAddUpdateActivity::class.java)
                resultLauncher.launch(intent)
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding?.root as View, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }

    private val noteObserver = Observer<List<Note>> {noteList ->
        if (noteList != null) {
            adapter.setListNote(noteList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityMainBinding = null
    }
}