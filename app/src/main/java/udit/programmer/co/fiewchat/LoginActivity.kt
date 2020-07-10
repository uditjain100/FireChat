package udit.programmer.co.fiewchat

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val db by lazy {
        FirebaseDatabase.getInstance().reference
    }

    lateinit var email: String
    lateinit var password: String

    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar_login)

        dialog = SpotsDialog.Builder().setContext(this).setContext(this).build()

        login_back.setOnClickListener {
            onBackPressed()
        }

        val sharedPreferences = getSharedPreferences("1000", Context.MODE_PRIVATE)
        email_input.setText(sharedPreferences.getString("email", "EMAIL"))
        password_input.setText(sharedPreferences.getString("customerpassword", "PASSWORD"))

        login_btn.setOnClickListener {
            if (checkEmptyField()) {
                dialog.show()
                email = email_input.text.toString()
                password = password_input.text.toString()

                sharedPreferences.edit { putString("email", email) }
                sharedPreferences.edit { putString("customerpassword", password) }

                login_btn.isEnabled = false

                auth.signInWithEmailAndPassword(
                    email_input.text.toString(),
                    password_input.text.toString()
                ).addOnSuccessListener {
                    dialog.dismiss()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "FAILED : $it", Toast.LENGTH_LONG).show()
                    login_btn.isEnabled = true
                }
            }
        }

    }

    private fun checkEmptyField(): Boolean {
        if (email_input.text.isNullOrBlank()) {
            email_input.error = "This field is Empty"
            return false
        }
        if (password_input.text.isNullOrBlank()) {
            password_input.error = "This field is Empty"
            return false
        }
        if (password_input.text.toString().length < 7) {
            password_input.error = "PassWord is too Short"
            return false
        }
        return true
    }


}