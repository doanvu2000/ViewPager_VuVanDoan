package com.example.noteapp_vuvandoan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_item_detail.*

class DetailNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_item_detail)
        val oldTitle = intent.getStringExtra("title") ?: ""
        val oldTimeNote = intent.getStringExtra("timeNote") ?: ""
        val oldContent = intent.getStringExtra("content") ?: ""
        val oldNote = Note(oldTitle, oldTimeNote, oldContent)
        inputDetailTitle.setText(oldTitle)
        inputDetailTimeNote.setText(oldTimeNote)
        inputDetailContentNote.setText(oldContent)
        btnUpdateNote.setOnClickListener {
            val newTitle = inputDetailTitle.text.toString()
            val newTimeNote = inputDetailTimeNote.text.toString()
            val newContent = inputDetailContentNote.text.toString()
            val newNote = Note(newTitle, newTimeNote, newContent)
            val status = NoteDatabase(this).updateNote(oldNote, newNote)
            startActivity(Intent(this, NoteHomeScreen::class.java))
            finish()
        }
    }
}