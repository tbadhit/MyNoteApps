package com.tbadhit.mynoteapps.repository

import android.app.Application
import androidx.paging.PagingSource
import com.tbadhit.mynoteapps.database.Note
import com.tbadhit.mynoteapps.database.NoteDao
import com.tbadhit.mynoteapps.database.NoteRoomDatabase
import com.tbadhit.mynoteapps.helper.SortUtils
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NoteRepository(application: Application) {
    private val mNotesDao: NoteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = NoteRoomDatabase.getDatabase(application)
        mNotesDao = db.noteDao()
    }

    fun getAllNotes(sort: String): PagingSource<Int, Note> {
        val query = SortUtils.getSortedQuery(sort)
        return mNotesDao.getAllNotes(query)
    }

    fun insert(note: Note) {
        executorService.execute { mNotesDao.insert(note) }
    }

    fun delete(note: Note) {
        executorService.execute { mNotesDao.delete(note) }
    }

    fun update(note: Note) {
        executorService.execute { mNotesDao.update(note) }
    }
}