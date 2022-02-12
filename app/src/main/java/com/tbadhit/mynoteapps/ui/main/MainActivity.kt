package com.tbadhit.mynoteapps.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tbadhit.mynoteapps.R
import com.tbadhit.mynoteapps.database.Note
import com.tbadhit.mynoteapps.databinding.ActivityMainBinding
import com.tbadhit.mynoteapps.helper.SortUtils
import com.tbadhit.mynoteapps.helper.ViewModelFactory
import com.tbadhit.mynoteapps.ui.insert.NoteAddUpdateActivity

class MainActivity : AppCompatActivity() {

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding

    private lateinit var mainViewModel: MainViewModel

//    private lateinit var adapter: NoteAdapter
    private lateinit var adapter: NotePagedListAdapter

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

        mainViewModel = obtainViewModel(this@MainActivity)
        mainViewModel.getAllNotes(SortUtils.NEWEST).observe(this, noteObserver)

        adapter = NotePagedListAdapter(this@MainActivity)

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

    private val noteObserver = Observer<PagingData<Note>> {noteList ->
        if (noteList != null) {
            adapter.submitData(lifecycle,noteList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var sort = ""
        when (item.itemId) {
            R.id.action_newest -> sort = SortUtils.NEWEST
            R.id.action_oldest -> sort = SortUtils.OLDEST
            R.id.action_random -> sort = SortUtils.RANDOM
        }
        mainViewModel.getAllNotes(sort).observe(this, noteObserver)
        item.isChecked = true
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityMainBinding = null
    }
}