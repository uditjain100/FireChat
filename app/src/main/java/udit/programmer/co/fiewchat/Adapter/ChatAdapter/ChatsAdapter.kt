package udit.programmer.co.fiewchat.Adapter.ChatAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import udit.programmer.co.fiewchat.Model.Message
import udit.programmer.co.fiewchat.R

class ChatsAdapter(var messagelist: MutableList<Message>) :
    RecyclerView.Adapter<ChatsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        return if (viewType == 1) {
            ChatsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_right, parent, false)
            )
        } else {
            ChatsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_messsage_right, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = messagelist.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
        return if (messagelist[position].senderID.equals(FirebaseAuth.getInstance().currentUser!!.uid)) 1 else 0
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.bind(messagelist[position])
    }
}