package udit.programmer.co.fiewchat.Adapter.User

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import udit.programmer.co.fiewchat.Interface.OnUserItemClickListener
import udit.programmer.co.fiewchat.Model.User
import udit.programmer.co.fiewchat.R

class UserSearchAdapter(var users: MutableList<User>) :
    RecyclerView.Adapter<UserSearchViewHolder>() {

    lateinit var onUserItemClickListener: OnUserItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {
        return UserSearchViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_search_layput, parent, false)
        )
    }

    override fun getItemCount(): Int = users!!.size

    override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {
        holder.bind(users!![position])
        holder.itemView.setOnClickListener {
            onUserItemClickListener.onClick(users!![position])
        }
    }


}