package udit.programmer.co.fiewchat.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_settings.*
import udit.programmer.co.fiewchat.Model.User
import udit.programmer.co.fiewchat.R

class SettingsFragment : Fragment() {

    private var uri: Uri? = null
    private lateinit var dialog: AlertDialog

    private lateinit var firebaseStorage: StorageReference
    private lateinit var filePath: StorageReference

    private lateinit var firebaseDatabase: DatabaseReference
    private lateinit var firebaseUser: FirebaseUser

    private var temp = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseStorage = FirebaseStorage.getInstance().reference
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("USERS")
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        dialog = SpotsDialog.Builder().setCancelable(false).setContext(requireContext()).build()

        retrieveData()
        profile_dp.isClickable = false
        cover_image.setOnClickListener {
            coverImageWork()
        }

    }

    private fun coverImageWork() {
        CropImage.activity().setAspectRatio(1, 1).start(requireContext(), this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (temp) {
                temp = false
                dialog.show()
                uri = CropImage.getActivityResult(data).uri
                Picasso.get().load(uri).into(cover_image)

                filePath = firebaseStorage.child("Users").child("Cover")
                    .child(firebaseUser.uid)
                filePath.putFile(CropImage.getActivityResult(data).uri).addOnCompleteListener {
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Uploading Failed $it :(", Toast.LENGTH_LONG)
                        .show()
                }.continueWithTask {
                    filePath.downloadUrl
                }.addOnCompleteListener {
                    if (it.isComplete) {
                        uri = it.result
                        updateImageValue()
                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Uri Failed $it :(", Toast.LENGTH_LONG).show()
                }
            } else {
                temp = true
                dialog.show()
                uri = CropImage.getActivityResult(data).uri
                Picasso.get().load(uri).into(profile_dp)

                filePath = firebaseStorage.child("Users").child("Profile")
                    .child(firebaseUser.uid)
                filePath.putFile(CropImage.getActivityResult(data).uri).addOnCompleteListener {
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Uploading Failed $it :(", Toast.LENGTH_LONG)
                        .show()
                }.continueWithTask {
                    filePath.downloadUrl
                }.addOnCompleteListener {
                    if (it.isComplete) {
                        uri = it.result
                        updateDPValue()
                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Uri Failed $it :(", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun updateDPValue() {
        val map = mutableMapOf<String, Any>()
        map["profile"] = uri!!.toString()
        firebaseDatabase.child(firebaseUser.uid).updateChildren(map)
            .addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(
                    requireContext(), "Uploaded Successfully :)", Toast.LENGTH_LONG
                ).show()
            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(
                    requireContext(), "Uploading Failed :(", Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun updateImageValue() {
        val map = mutableMapOf<String, Any>()
        map["cover"] = uri!!.toString()
        firebaseDatabase.child(firebaseUser.uid).updateChildren(map)
            .addOnSuccessListener {
                profile_dp.isClickable = true
                dialog.dismiss()
                Toast.makeText(
                    requireContext(), "Uploaded Successfully :)", Toast.LENGTH_LONG
                ).show()
            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(
                    requireContext(), "Uploading Failed :(", Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun retrieveData() {
        FirebaseDatabase.getInstance().getReference("USERS")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    name_profile.text = user!!.name
                    number_profile.text = user.phoneNumber
                    if (user.cover != "") Picasso.get().load(user.cover.toUri()).into(cover_image)
                    if (user.profile != "") Picasso.get().load(user.profile.toUri())
                        .into(profile_dp)
                }
            })
    }

}