package com.example.noteapp_vuvandoan

import adapter.NoteAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_note_home_screen.*

class NoteHomeScreen : AppCompatActivity() {
    private val TAG = "NoteHomeScreen"
    var noteList: MutableList<Note> = ArrayList()
    var searchNoteList: MutableList<Note> = ArrayList()
    lateinit var noteDatabase: NoteDatabase
    lateinit var noteAdapter: NoteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_home_screen)

        //initData
        //  initDataList()
        noteDatabase = NoteDatabase(this)
        fetchAllNoteFromDatabase()
        noteAdapter = NoteAdapter(noteList)
        noteAdapter.setOnClickItem {
            Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DetailNoteActivity::class.java)
            intent.putExtra("title", it.title)
            intent.putExtra("timeNote", it.timeNote)
            intent.putExtra("content", it.content)
            startActivity(intent)
        }
//        searchNoteList = noteList
        rcv_notes.adapter = noteAdapter
        rcv_notes.layoutManager = LinearLayoutManager(this)
        rcv_notes.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        search_bar.setOnQueryTextListener(object : OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "onQueryTextSubmit: $query")
                search_bar.clearFocus()
                //click submit string search
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextChange: $newText")
                if (newText != null) {
                    searchNoteList = searchNote(newText)
                    noteAdapter.noteList = searchNoteList
                    noteAdapter.notifyDataSetChanged()
                }
                return true
            }
        })
        btnAddNote.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
    }

    private fun fetchAllNoteFromDatabase() {
        noteList = noteDatabase.getAllNote()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun searchNote(s: String): MutableList<Note> =
        noteList.filter { note ->
            note.content.lowercase().contains(s.lowercase())
        } as MutableList<Note>
}