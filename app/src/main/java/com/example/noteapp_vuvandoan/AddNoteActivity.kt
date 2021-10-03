package com.example.noteapp_vuvandoan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*

class AddNoteActivity : AppCompatActivity() {
    lateinit var sqliteHelper: NoteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        sqliteHelper = NoteDatabase(this)
        btnAdd.setOnClickListener {
            addNote()
        }
        val day = intent.getIntExtra("dayNote", 1).toString()
        val month = intent.getIntExtra("monthNote", 1)
        val year = intent.getIntExtra("yearNote", 2021)
        inputTimeNote.setText("$day/$month/$year")
    }

    private fun addNote() {
        val title = inputTitleNote.text.toString()
        val timeNote = inputTimeNote.text.toString()
        val contentNote = inputContentNote.text.toString()
        if (title.isEmpty() || timeNote.isEmpty() || contentNote.isEmpty()) {
            Toast.makeText(
                this,
                "Bạn phải nhập đủ thông tin 3 trường", Toast.LENGTH_SHORT
            ).show()
        } else {
            val status = sqliteHelper.insertNote(Note(title, timeNote, contentNote))
            if (status > -1) {
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
                clearEditext()
            } else {
                Toast.makeText(this, "Không thêm được", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, NoteHomeScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }

    private fun clearEditext() {
        inputTitleNote.setText("")
        inputTimeNote.setText("")
        inputContentNote.setText("")
        inputTitleNote.requestFocus()
    }
}