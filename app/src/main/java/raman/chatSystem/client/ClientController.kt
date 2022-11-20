package raman.chatSystem.client

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import raman.chatSystem.adapter.messagesAdapter.RecyclerAdapter
import raman.chatSystem.fragments.phoneSignUp.Controller_CodeVerfiy
import raman.chatSystem.fragments.singleChat.Controller_SingleChat
import raman.chatSystem.model.LiveViewModel
import raman.chatSystem.model.messages.Message
import raman.chatSystem.model.messages.MessageType
import java.util.*

class ClientController(controllerCodeVerfiy: Controller_CodeVerfiy)
{
    val communicationManager = ClientCommunicationManager(controllerCodeVerfiy)
    //LiveClient
    //private val client = ViewModelProvider(controllerCodeVerfiy.fragment_.requireActivity())[LiveViewModel::class.java].client
    //View || recyclerAdapter
    //List<users>
    lateinit var clientListener: ClientListener
    lateinit var clientSender: ClientSender
    private lateinit var client : Client

    lateinit var controller_singleChat: Controller_SingleChat

    private val myViewModel = ViewModelProvider(controllerCodeVerfiy.fragment_.requireActivity())[LiveViewModel::class.java]
    private val TAG = "ClientController"

    init {
        connectClient()
    }

    fun set_reader_writer()
    {
        Log.d(TAG, "Reader: ${client.reader}, Writer: ${client.writer}")
        clientListener = ClientListener(this, client.reader)
        clientListener.start()
        clientSender = ClientSender(this, client.writer)
        clientSender.start()
    }

    fun sendMessage(user: String, message:String)
    {
        Log.d(TAG, "sendMessage(): user $user, message: $message")
        clientSender.sendMessage("$user: $message")
    }

    fun createClient()
    {
        //Start client
        client = Client(communicationManager)
        client.start()
        myViewModel.set_Current_Client(client)
    }

    fun connectClient()
    {
        Log.d(TAG, "Before the crash...")
        createClient()
        Log.d(TAG, "Client the crash...")
        Thread.sleep(2000)
        set_reader_writer()
    }

    fun closeConnection()
    {
        try {
            if (!clientSender.isInterrupted)
                clientSender.interrupt()
            if (!clientListener.isInterrupted)
                clientListener.interrupt()
        }catch (e: Exception)
        {
            Log.d(TAG, "Closing read and write exception")
        }
    }

    fun removeServer()
    {
        if(myViewModel.clientController.value != null)
            myViewModel.set_Current_Client(null)
        Log.d(TAG, "removeServer(), Writer: ${myViewModel.clientController.value}")
    }

    // ################## Listening #################
    fun filterMessage(msg: String)
    {
        val breakPoint = msg.indexOf(":")
        val user = msg.substring(0, breakPoint)
        val message = msg.substring(breakPoint + 1)
        Log.d(TAG,"filterMessage() User $user --- Message: $message");
        //Send the message to be added
        addMessage(user, message)
    }

    private fun addMessage(user: String, msg: String)
    {
        controller_singleChat.addMessage(msg, MessageType.RECEIVED_MESSAGE)
        Log.d(TAG,"addMessage() User $user --- Message: $msg");
    }
}