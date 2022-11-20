package raman.chatSystem.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import raman.chatSystem.client.Client
import raman.chatSystem.client.ClientController
import raman.chatSystem.model.messages.MessageCenter
import raman.chatSystem.model.messages.MessageType

class LiveViewModel: ViewModel()
{
    //List of available planets.
    val selectedUser = MutableLiveData<User>()
    val client = MutableLiveData<Client?>()
    val clientController = MutableLiveData<ClientController>()
    val users = MutableLiveData<List<User>>()

    init {
        users.value = Users.getUsers()
    }

    fun selectChatUser(user: User)
    {
        selectedUser.value = user
    }

    fun set_Current_Client(client_: Client?)
    {
        client.value = client_
    }

    fun set_Client_Controller(clientCon: ClientController)
    {
        clientController.value = clientCon
    }
}