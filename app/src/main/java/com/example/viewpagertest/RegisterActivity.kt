package com.example.viewpagertest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.viewpagertest.api.ProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etMail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etContact)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)

        findViewById<Button>(R.id.register).setOnClickListener {
            val textPassword = etPassword.text.toString()
            val textConfirmPassword = etConfirmPassword.text.toString()
            if(textPassword == textConfirmPassword) {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = ProfileApi.createProfile(etUsername.text.toString(),etMail.text.toString().replace(" ", ""), etPhone.text.toString(), etPassword.text.toString())
                    withContext(Dispatchers.Main) {
                        if(response[0].toString().toBoolean()) {
                            Toast.makeText(this@RegisterActivity, response[1].toString(), Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, response[1].toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Password And Confirm Password Not Match", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<LinearLayout>(R.id.signIn).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }
}