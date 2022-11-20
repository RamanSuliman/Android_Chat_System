package raman.chatSystem.client

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.Socket

class ClientListener(private val clientController: ClientController, private val reader: BufferedReader) : Thread()
{
    private val TAG = "ClientListener"

    override fun run()
    {
        try {
            Log.d(TAG, reader.readLine())
            while (!isInterrupted)
            {
                val message = reader.readLine() ?: break
                //Add the message into the window
                clientController.filterMessage(message)
            }
        }catch (e: IOException)
        {
            Log.d(TAG, "Listening error occurred, thread is interrupted.")
            //Broadcasting server has disconnected
            //connectionState.serverDisconnected()
            e.printStackTrace()
        }
        Log.d(TAG, "ClientListener lost connection with server.")
        // Communication is broken. Interrupt both listener and sender threads
        clientController.closeConnection()
        //Set server connection to null
        clientController.removeServer()
    }
}