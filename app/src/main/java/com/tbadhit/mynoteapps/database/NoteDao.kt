package com.tbadhit.mynoteapps.database

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @RawQuery(observedEntities = [Note::class])
    fun getAllNotes(query: SupportSQLiteQuery): PagingSource<Int, Note>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(list: List<Note>)
}