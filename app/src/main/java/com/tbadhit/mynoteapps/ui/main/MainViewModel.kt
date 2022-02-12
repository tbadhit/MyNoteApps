package com.tbadhit.mynoteapps.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.tbadhit.mynoteapps.database.Note
import com.tbadhit.mynoteapps.repository.NoteRepository

class MainViewModel(application: Application) : ViewModel() {
    private val mNoteRepository: NoteRepository = NoteRepository(application)

    fun getAllNotes(sort: String): LiveData<PagingData<Note>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { mNoteRepository.getAllNotes(sort) }
    ).liveData
}