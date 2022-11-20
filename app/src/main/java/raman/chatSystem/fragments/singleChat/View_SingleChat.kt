package raman.chatSystem.fragments.singleChat

import android.text.TextUtils
import androidx.recyclerview.widget.DividerItemDecoration
import raman.chatSystem.R
import raman.chatSystem.adapter.messagesAdapter.RecyclerAdapter
import raman.chatSystem.databinding.FragmentSingleChatBinding

class View_SingleChat (private val binding: FragmentSingleChatBinding, private val fragment: SingleChatFragment)
{
    var inputMessage = binding.inputMessage
    var btnSendMessage = binding.sendMessage
    var recyclerView = binding.chatRecycleView
    //var

    fun setup_RecycleView(username: String,adapter: RecyclerAdapter)
    {
        binding.textName.text = username
        val recyclerView = binding.chatRecycleView
        recyclerView.adapter = adapter
        // True if adapter changes cannot affect the size of the RecyclerView
        recyclerView.setHasFixedSize(true)
        //Make smooth scrolling
        recyclerView.isNestedScrollingEnabled = false
        //################ Add a liner between each view holder ##############
        //recyclerView.addItemDecoration(DividerItemDecoration(context, 1)) //Default method
        val itemDecoration = DividerItemDecoration(fragment.requireContext(), DividerItemDecoration.VERTICAL)
        @Suppress("UseCompatLoadingForDrawables") itemDecoration.setDrawable(fragment.requireActivity().resources.getDrawable(R.drawable.liner))
        recyclerView.addItemDecoration(itemDecoration)
        //itemDecoration.onDrawOver()     //When drawing finished

        //Scroll to the last message when opening the chat
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }

    fun isInputMessage_Empty(): Boolean
    {
        return TextUtils.isEmpty(inputMessage.text.toString())
    }

    fun get_input_message(): String
    {
        return inputMessage.text.toString().trim()
    }

    fun clear_input_message()
    {
        //Empty the input text area
        binding.inputMessage.text.clear()
    }

    fun scroll_recycleView(toPosition: Int)
    {
        //Scroll to the last message
        recyclerView.scrollToPosition(toPosition)
    }
}