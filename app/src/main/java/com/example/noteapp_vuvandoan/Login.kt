package com.example.noteapp_vuvandoan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setUpPassCodeInput()
        val share = getSharedPreferences("PassWord", MODE_PRIVATE)
        btnLogin.setOnClickListener {
            val pass = inputCode1.text.toString() + inputCode2.text.toString() +
                    inputCode3.text.toString() + inputCode4.text.toString()
            if (pass.length != 4) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu gồm 4 số", Toast.LENGTH_SHORT).show()
            } else {
                val isSetPass = share.getBoolean("isSetPassword", false)
                val localPass = if (!isSetPass) "0000" else share.getString("password", "")
                Log.d("pass", "$pass - $localPass: ")
                if (pass == localPass) {
                    //login
                    startActivity(Intent(this, NoteHomeScreen::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Mật khẩu chưa chính xác", Toast.LENGTH_SHORT).show()
                }
            }

        }
        btnChangePass.setOnClickListener {//change PassWord to Login
            startActivity(Intent(this, ChangePassActivity::class.java))
            finish()
        }
    }

    fun setUpPassCodeInput() {
        inputCode1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    inputCode2.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        inputCode2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    inputCode3.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        inputCode3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    inputCode4.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }
}