package udit.programmer.co.fiewchat.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search.*
import udit.programmer.co.fiewchat.Adapter.User.UserSearchAdapter
import udit.programmer.co.fiewchat.Interface.OnUserItemClickListener
import udit.programmer.co.fiewchat.Model.User
import udit.programmer.co.fiewchat.R

class SearchFragment : Fragment() {

    private lateinit var pgAdapter: UserSearchAdapter
    private var usersList = mutableListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userSearchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchUsers(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.equals("")) usersList.clear()
                newText?.let { searchUsers(it) }
                return true
            }
        })


        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        user_search_recycler_view.layoutManager = layoutManager
        user_search_recycler_view.setHasFixedSize(true)

        pgAdapter = UserSearchAdapter(usersList)
        pgAdapter.onUserItemClickListener = object : OnUserItemClickListener {
            override fun onClick(user: User) {

            }
        }
        user_search_recycler_view.adapter = pgAdapter
    }

    private fun searchUsers(it: String): Any {
        val usersReference = FirebaseDatabase.getInstance().reference.child("USERS")

        val query = FirebaseDatabase.getInstance().reference
            .child("USERS")
            .orderByChild("name")
            .startAt(it)
            .endAt(it + "\uf8ff")

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usersList.clear()
                for (snapShot in dataSnapshot.children) {
                    val pg = snapShot.getValue(User::class.java)
                    if (pg != null)
                        usersList.add(pg)
                }
            }
        })
        return usersReference
    }

}