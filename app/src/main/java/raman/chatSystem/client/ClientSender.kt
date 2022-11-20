package raman.chatSystem.client

import android.util.Log
import java.io.IOException
import java.io.PrintWriter
import java.util.*

class ClientSender (private val clientController: ClientController ,private val writer: PrintWriter /*, private val connectionState: ConnectionState*/) : Thread()
{
    private val messageQueue: Vector<String> = Vector<String>()
    private val lock = Object()

    private val TAG = "ClientSender"

    /**
     * Until interrupted reads messages from the standard input (keyboard)
     * and sends them to the chat server through the socket.
     */
    override fun run() {
        try {
            //val reader = BufferedReader(InputStreamReader(System.`in`))
            //print("What is your name? >>>> ")
            //writer.println(reader.readLine())
            while (!isInterrupted)
            {
                //val message: String = reader.readLine()
                //writer.println(message)
                val message = getNextMessageFromQueue()
                Log.d(TAG,"Run: $message")
                sendMessageToClient(message!!)
            }
        } catch (e: IOException) {
            Log.d(TAG,"Connection to server broken, can't send messages.")
            //Broadcast Server has disconnected
            //connectionState.serverDisconnected()
            e.printStackTrace()
        }
        // Communication is broken. Interrupt both listener and sender threads
        clientController.closeConnection()
        //Set server connection to null
        clientController.removeServer()
    }

    /*
     * Adds given message to the message queue and notifies this thread
     * (actually getNextMessageFromQueue method) that a message is arrived.
     *  sendMessage is called by other threads (ServeDispatcher).
     */
    fun sendMessage(message: String?)
    {
        synchronized(lock)
        {
            Log.d(TAG,"sendMessage: $message")
            messageQueue.add(message)
            Log.d(TAG,"In queue: ${messageQueue.lastElement()}")
            try{
                lock.notify()
            }catch (e: IllegalMonitorStateException )
            {
                Log.d(TAG,"Lock---> IllegalMonitorStateException")
                e.printStackTrace()
            }
        }
    }

    /**
     * @return and deletes the next message from the message queue. If the queue
     * is empty, falls in sleep until notified for message arrival by sendMessage
     * method.
     */
    @Synchronized
    private fun getNextMessageFromQueue(): String?
    {
        Log.d(TAG,"getNextMessageFromQueue() is locked")
        synchronized(lock)
        {
            while (messageQueue.size == 0)
                try{
                    Log.d(TAG,"getNextMessageFromQueue() waiting...")
                    lock.wait()
                }catch (e: InterruptedException) {
                    Log.d(TAG, "ClientSender object is interrupted at getNextMessageFromQueue().")
                }
            val message = messageQueue[0]
            messageQueue.removeElementAt(0)
            Log.d(TAG,"getNextMessageFromQueue() returned message: $message")
            return message
        }
    }

    /**
     * Sends given message to the client's socket.
     */
    private fun sendMessageToClient(message: String)
    {
        Log.d(TAG,"sendMessageToClient() final sending message: $message")
        writer.println(message)
    }
}