package udit.programmer.co.fiewchat.Adapter.ChatAdapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_message_right.view.*
import kotlinx.android.synthetic.main.item_messsage_right.view.*
import udit.programmer.co.fiewchat.Model.Message

class ChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(message: Message) {

        if (message.message.equals("sent you an image") && message.url != "") {
            if (message.senderID.equals(FirebaseAuth.getInstance().currentUser!!.uid)) {
                itemView.text_message_right.visibility = View.GONE
                itemView.text_message_right.visibility = View.VISIBLE
                Picasso.get().load(message.url).into(itemView.profile_image_right_message)
            } else {
                itemView.text_message_left.visibility = View.GONE
                itemView.text_message_left.visibility = View.VISIBLE
                Picasso.get().load(message.url).into(itemView.profile_image_left_message)
            }
        } else {
            itemView.text_message_left.text = message.message
        }

    }
}