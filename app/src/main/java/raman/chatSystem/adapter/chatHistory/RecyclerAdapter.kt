package raman.chatSystem.adapter.chatHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import raman.chatSystem.databinding.UserContainerBinding

import raman.chatSystem.model.User
import kotlin.random.Random

class RecyclerAdapter(private val users: List<User>): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private lateinit var onChatClickListener: ChatClickListener

    fun setClickListener(listener: ChatClickListener)
    {
        onChatClickListener = listener
    }

    inner class MyViewHolder(val binding: UserContainerBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val binding = UserContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        val user = users[position]
        val userContainerView = (holder as MyViewHolder)
        userContainerView.binding.message.text = "Demo Text from the code, it will be replaced by an actual user message later"
        userContainerView.binding.name.text = user.username
        user.numOfUnreadMessages = Random.nextInt(4, 99)
        userContainerView.binding.numberOfUnreadMessages.text = user.numOfUnreadMessages.toString()
        userContainerView.binding.profilePic.setImageResource(user.picture)
        userContainerView.itemView.setOnClickListener {
            onChatClickListener.onChatClickListener(user)
        }
    }

    override fun getItemCount(): Int = users.size

}