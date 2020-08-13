package udit.programmer.co.fiewchat

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_message.*
import udit.programmer.co.fiewchat.Model.Message
import udit.programmer.co.fiewchat.Model.User

class MessageActivity : AppCompatActivity() {

    private val cUser by lazy {
        FirebaseAuth.getInstance().currentUser
    }
    private val db by lazy {
        FirebaseDatabase.getInstance().reference
    }
    private val storageRef by lazy {
        FirebaseStorage.getInstance().reference
    }
    private lateinit var dialog: AlertDialog
    private var userUid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(R.layout.activity_message)

        dialog = SpotsDialog.Builder().setCancelable(false).setContext(this).build()

        back_btn_message.setOnClickListener {
            onBackPressed()
        }

        userUid = intent.getStringExtra("Ceased Meteor")!!
        retrieveData(userUid)

        send_btn_message.setOnClickListener {
            if (checkEmotyField())
                sendMessageToUser()
            message_input_field_message.setText("")
        }

        send_image_btn_message.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK), 1234)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK && data != null) {
            dialog.show()

            val uri = data.data
            val messageId = db.push().key
            val filePath = storageRef.child("Chat Images").child("$messageId.jpg")

            filePath.putFile(uri!!).addOnCompleteListener {
            }.addOnFailureListener {
                Toast.makeText(this, "Uploading Failed $it :(", Toast.LENGTH_LONG)
                    .show()
            }.continueWithTask {
                filePath.downloadUrl
            }.addOnCompleteListener {
                if (it.isComplete) {
                    val message = Message(
                        senderID = cUser!!.uid,
                        recieverID = userUid,
                        message = "sent you an image.",
                        isSeen = false,
                        url = uri.toString(),
                        messageID = messageId!!
                    )
                    db.child("CHATS").child(messageId).setValue(message)
                }
                dialog.dismiss()
            }.addOnFailureListener {
                Toast.makeText(this, "Uri Failed $it :(", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }

        }
    }

    private fun sendMessageToUser() {

        val messageKey = db.push().key!!
        val message = Message(
            senderID = cUser!!.uid,
            recieverID = userUid,
            message = message_input_field_message.text.toString(),
            isSeen = false,
            url = "",
            messageID = messageKey
        )
        db.child("CHATS").child(messageKey).setValue(message)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val chatListSenderRef = FirebaseDatabase.getInstance().getReference("ChatList")
                        .child(cUser!!.uid).child(userUid)
                    chatListSenderRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {}
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists())
                                chatListSenderRef.child("id").setValue(userUid)

                            val chatListRecieverRef =
                                FirebaseDatabase.getInstance().getReference("ChatList")
                                    .child(userUid).child(cUser!!.uid)
                            chatListRecieverRef.child("id").setValue(cUser!!.uid)
                        }
                    })

                    //Notification Work
                    val userRef =
                        FirebaseDatabase.getInstance().getReference("USERS").child(cUser!!.uid)
                }
            }
    }

    private fun retrieveData(userUid: String?) {
        FirebaseDatabase.getInstance().getReference("USERS").child(userUid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    Picasso.get().load(user!!.profile.toUri()).into(user_dp_message)
                    user_name_message.text = user.userName
                }
            })
    }

    private fun checkEmotyField(): Boolean {
        if (message_input_field_message.text.toString().isEmpty()) {
            message_input_field_message.error = "Please write message first!!"
            return false
        }
        return true
    }
}