package udit.programmer.co.fiewchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_register.*
import udit.programmer.co.fiewchat.Model.User

class RegisterActivity : AppCompatActivity() {

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    private val db by lazy {
        FirebaseDatabase.getInstance()
    }
    private val users by lazy {
        db.getReference("USERS")
    }

    lateinit var dialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar_register)

        dialog = SpotsDialog.Builder().setCancelable(false).setContext(this).build()

        register_back.setOnClickListener {
            onBackPressed()
        }

        register_btn.setOnClickListener {
            if (checkEmptyField()) {
                dialog.show()
                auth.createUserWithEmailAndPassword(
                    email_input.text.toString(), passWord_input.text.toString()
                ).addOnCompleteListener {
                    val user = User(
                        uid = auth.currentUser!!.uid,
                        userName = username_input.text.toString(),
                        name = name_input.text.toString(),
                        phoneNumber = mobileNumber_input.text.toString(),
                        email = email_input.text.toString(),
                        passWord = passWord_input.text.toString()
                    )
                    users.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user)
                        .addOnCompleteListener {
                            dialog.dismiss()
                            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_LONG)
                                .show()
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                }
            }
        }
    }

    private fun checkEmptyField(): Boolean {
        if (name_input.text.isNullOrBlank()) {
            name_input.error = "This field is Empty"
            return false
        }
        if (mobileNumber_input.text.isNullOrBlank()) {
            mobileNumber_input.error = "This field is Empty"
            return false
        }
        if (mobileNumber_input.text.toString().length != 10) {
            mobileNumber_input.error = "Mobile Number is Invalid"
            return false
        }
        if (email_input.text.isNullOrBlank()) {
            email_input.error = "This field is Empty"
            return false
        }
        if (passWord_input.text.isNullOrBlank()) {
            passWord_input.error = "This field is Empty"
            return false
        }
        if (passWord_input.text.toString().length < 7) {
            passWord_input.error = "Password length should be greater than 6"
            Toast.makeText(this, "Password id too Short", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}
