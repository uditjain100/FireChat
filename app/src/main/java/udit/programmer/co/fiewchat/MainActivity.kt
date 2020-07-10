package udit.programmer.co.fiewchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import udit.programmer.co.fiewchat.Adapter.ViewPagerAdapter
import udit.programmer.co.fiewchat.Fragments.ChatFragment
import udit.programmer.co.fiewchat.Fragments.SearchFragment
import udit.programmer.co.fiewchat.Fragments.SettingsFragment
import udit.programmer.co.fiewchat.Model.User

class MainActivity : AppCompatActivity() {

    lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)
        toolbar_main.title = ""

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewPagerAdapter.addFragment(ChatFragment(), "Chats")
        viewPagerAdapter.addFragment(SearchFragment(), "Search")
        viewPagerAdapter.addFragment(SettingsFragment(), "Settings")

        view_pager_main.adapter = viewPagerAdapter
        tab_layout_main.setupWithViewPager(view_pager_main)

        retrieveData()
    }

    private fun retrieveData() {
        FirebaseDatabase.getInstance().getReference("USERS")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    user_name_main.text = user!!.userName
                    if (user.profile != "") Picasso.get().load(user.profile.toUri())
                        .into(user_dp_main)
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, WelcomeActivity::class.java))
                true
            }
            R.id.action_settings -> true
            else -> false
        }
        super.onOptionsItemSelected(item)
    }

}