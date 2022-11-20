package raman.chatSystem.adapter.messagesAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import raman.chatSystem.databinding.ItemContainerReceivedMessageBinding
import raman.chatSystem.databinding.ItemContainerSentMessageBinding
import raman.chatSystem.model.messages.MessageCenter
import raman.chatSystem.model.messages.MessageType

class RecyclerAdapter(private val messages: ArrayList<MessageCenter>): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private lateinit var bindingSentMessage: ItemContainerSentMessageBinding
    private lateinit var bindingReceivedMessage: ItemContainerReceivedMessageBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        return when(viewType) {
            MessageType.SENT_MESSAGE.typeValue -> {
                //An inflater allows you to create a View from a resource layout file
                bindingSentMessage = ItemContainerSentMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                //This will be returned
                SentMessageViewHolder(bindingSentMessage)
            }
            MessageType.RECEIVED_MESSAGE.typeValue -> {
                bindingReceivedMessage = ItemContainerReceivedMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                //This will be returned
                ReceivedMessageViewHolder(bindingReceivedMessage)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        val message = messages[position]
        if(message.type == MessageType.SENT_MESSAGE)
            (holder as SentMessageViewHolder).binding.textMessage.text = message.message
        else
            (holder as ReceivedMessageViewHolder).binding.textMessage.text = message.message
    }

    override fun getItemCount(): Int = messages.size

    fun deleteItem(pos:Int)
    {
        messages.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun addItem(message: MessageCenter)
    {
        messages.add(message)
        notifyItemInserted(messages.lastIndex)
    }

    override fun getItemViewType(position: Int): Int
    {
        val message = messages[position]
        return message.type.typeValue
    }

}