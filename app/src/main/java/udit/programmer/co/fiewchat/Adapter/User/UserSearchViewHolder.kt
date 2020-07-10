package udit.programmer.co.fiewchat.Adapter.User

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_user_search_layput.view.*
import udit.programmer.co.fiewchat.Model.User

class UserSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(user: User) {
        itemView.item_name_customer.text = user.name
        itemView.item_username_customer.text = user.userName
    }
}