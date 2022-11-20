package raman.chatSystem.client

import raman.chatSystem.fragments.phoneSignUp.Controller_CodeVerfiy

class ClientCommunicationManager(private val controller: Controller_CodeVerfiy): ConnectionState
{
    override fun clientConnected()
    {
        controller.printout_server_status("connected")
    }

    override fun clientDisconnected()
    {
        controller.printout_server_status("client_disconnected")
    }

    override fun isServerDown()
    {
        controller.printout_server_status("down")
    }

    override fun serverDisconnected()
    {
        controller.printout_server_status("server_disconnected")
    }

    override fun serverHasUnknownMatters() {
        controller.printout_server_status("unknown")
    }
}