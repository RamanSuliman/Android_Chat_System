package raman.chatSystem.fragments.singleChat

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import raman.chatSystem.adapter.messagesAdapter.RecyclerAdapter
import raman.chatSystem.client.ClientController
import raman.chatSystem.model.LiveViewModel
import raman.chatSystem.model.User
import raman.chatSystem.model.messages.Message
import raman.chatSystem.model.messages.MessageCenter
import raman.chatSystem.model.messages.MessageType
import java.util.*
import kotlin.collections.ArrayList

class Controller_SingleChat(private val view: View_SingleChat, private val fragment: SingleChatFragment)
{
    private lateinit var messages:ArrayList<MessageCenter>
    private lateinit var myViewModel: LiveViewModel
    private lateinit var user : MutableLiveData<User>
    private lateinit var adapter: RecyclerAdapter
    private lateinit var clientController: MutableLiveData<ClientController>

    private val TAG = "single_chat"

    init {
        //Set up live data
        setupLiveModel()
        //Attach user messages to the adapter and create the view
        setupMessageAdapter()
        //
        set_action_listeners()
    }

    private fun setupLiveModel()
    {
        myViewModel = ViewModelProvider(fragment.requireActivity())[LiveViewModel::class.java]
        user = myViewModel.selectedUser
        clientController = myViewModel.clientController
        clientController.value?.controller_singleChat = this
        if(user.value != null)
            Log.d(TAG, "{live} User")
        if(clientController.value != null)
            Log.d(TAG, "{live} Client controller")
    }

    private fun setupMessageAdapter()
    {
        //Get selected user messages
        messages = user.value?.messages!!
        //val messages = user.value?.messages
        adapter = RecyclerAdapter(messages)
        //Init recycle view
        view.setup_RecycleView(user.value!!.username, adapter)
    }

    private fun set_action_listeners()
    {
        view.inputMessage.setOnClickListener{
            //Scroll to the last message when keyboard showed up
            view.recyclerView.scrollToPosition(messages.size - 1)
            Toast.makeText(fragment.requireContext(),  "Selected..", Toast.LENGTH_SHORT).show()
        }

        view.btnSendMessage.setOnClickListener{
            if(!view.isInputMessage_Empty())
            {
                //Add message to view
                addMessage(view.get_input_message(), MessageType.SENT_MESSAGE)

                //Send to user
                Log.d(TAG,"Send button: username-> ${user.value!!.username}, message-> ${view.get_input_message()}")
                clientController.value?.sendMessage(user.value!!.username, view.get_input_message()) ?: Log.d(TAG,"nuller")

                //Empty the input text area
                view.clear_input_message()
            }
        }
    }

    fun addMessage(message: String, messageType: MessageType)
    {
        Log.d(TAG,"addMessage() Add message to user-> ${user.value!!.username}, message-> $message")
        val msg = Message(message, adapter.itemCount + 1, Calendar.getInstance().time.toString(), messageType)

        Log.d(TAG,"Selected User: ${user.value?.username}")
        user.value?.messages?.add(msg)

        Log.d(TAG,"################################### Main chat #################################")
        myViewModel.users.value!!.first().messages.forEach{
            Log.d(TAG, it.message)
        }

        Log.d(TAG,"################################### Others #################################")
        val size = myViewModel.users.value!![1].messages.size
        Log.d(TAG,"User ${myViewModel.users.value!![1].username}")
        Log.d(TAG,"Last message ${myViewModel.users.value!![1].messages[size - 1].message}")
        Log.d(TAG,"......")
        val sizer = myViewModel.users.value!![2].messages.size
        Log.d(TAG,"User ${myViewModel.users.value!![2].username}")
        Log.d(TAG,"Last message ${myViewModel.users.value!![2].messages[sizer - 1].message}")

        Log.d(TAG,"################################### ------ #################################")
        //Adding the new message into the adapter list
        //adapter.addItem(msg)

        //Scroll to the last message
        fragment.requireActivity().runOnUiThread {
            view.scroll_recycleView(messages.size - 1)
        }
    }
}