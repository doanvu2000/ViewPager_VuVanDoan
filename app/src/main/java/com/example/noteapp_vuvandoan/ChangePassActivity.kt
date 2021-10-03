package com.example.noteapp_vuvandoan

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_change_pass.*

class ChangePassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)
        val share = getSharedPreferences("PassWord", MODE_PRIVATE)
        val edit = share.edit()
        setUpPassCodeInput()
        btnConfirm.setOnClickListener {
            val pass = input1.text.toString() + input2.text.toString() +
                    input3.text.toString() + input4.text.toString()
            if (pass.length != 4) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu gồm 4 số", Toast.LENGTH_SHORT).show()
            } else {
                val isSetpass = share.getBoolean("isSetPassword", false)
                if (!isSetpass) {//don't set pass => defaul password = 0000
                    if (pass != "0000") {
                        Toast.makeText(this, "Mật khẩu mặc định là 0000", Toast.LENGTH_SHORT).show()
                    } else {//default pass
                        setPass(edit)
                    }
                } else { //compare localPass
                    val localPass = share.getString("password", "")
                    if (pass == localPass) {
                        setPass(edit)
                    } else {
                        Toast.makeText(this, "Mật khẩu hiện tại chưa chính xác", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
        btnBackToLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    private fun setPass(edit : SharedPreferences.Editor) {
        val newPass = confirmCode1.text.toString() + confirmCode2.text.toString() +
                confirmCode3.text.toString() + confirmCode4.text.toString()
        if (newPass.length != 4) {
            Toast.makeText(
                this,
                "Vui lòng nhập mật khẩu gồm 4 số",
                Toast.LENGTH_SHORT
            ).show()
        } else {//set newPass
            edit.putString("password", newPass)
            edit.putBoolean("isSetPassword", true)
            edit.apply()
            Toast.makeText(this, "Mật khẩu hiện tại: $newPass", Toast.LENGTH_SHORT)
                .show()
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    fun setUpPassCodeInput() {
        confirmCode1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    confirmCode2.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        confirmCode2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    confirmCode3.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        confirmCode3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    confirmCode4.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        confirmCode4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    input1.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        input1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    input2.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        input2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    input3.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        input3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    input4.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }
}