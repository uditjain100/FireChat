package udit.programmer.co.fiewchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    lateinit var dialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()

        login_btn_welcome.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        register_btn_welcome.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }

    override fun onStart() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            dialog.show()
            startActivity(Intent(this, MainActivity::class.java))
            dialog.dismiss()
        }
        super.onStart()
    }
}