package raman.chatSystem.client

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class Client(private val connectionState: ConnectionState): Thread()
{
    //private val SERVER_HOSTNAME = "192.168.1.123"
    private val SERVER_HOSTNAME = "192.168.0.23"
    private val SERVER_PORT = 2022
    lateinit var reader: BufferedReader
    lateinit var writer: PrintWriter

    private val TAG = "Client_user"

    private var connected = false

    lateinit var serverSocket: Socket

    @RequiresApi(Build.VERSION_CODES.O)
    override fun run()
    {
        if(!create_client().also { connected = it })
        {
            Log.d(TAG, "Server is down, can't connect client.")
            //else start a fragment indicating server is down/unreachable
            // https://stackoverflow.com/questions/46551228/how-to-pass-and-get-value-from-fragment-and-activity
            return
        }
        val timeStamp = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
            .withZone(ZoneOffset.UTC)
            .format(Instant.now())
        Log.d(TAG, "Client made $connected. $timeStamp   ------- $reader")
    }

    private fun create_client() :Boolean
    {
        try {
            Log.d(TAG, "Trying to connect...")
            //Connect to server
            serverSocket = Socket()
            serverSocket.connect(InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 5000)
            reader = BufferedReader(InputStreamReader(serverSocket.getInputStream()))
            writer = PrintWriter(serverSocket.getOutputStream(), true)
            writer.println("User")
            //println("Connected to server $SERVER_HOSTNAME:$SERVER_PORT")
            Log.d(TAG,"Connected to server $SERVER_HOSTNAME:$SERVER_PORT")
            Log.d(TAG, "Made the connection!")
            //Broadcasting client has connected with the server
            connectionState.clientConnected()
        }catch (e: SocketTimeoutException) {
            Log.d(TAG, "Timeout failed to connect to server at $SERVER_HOSTNAME")
            //Broadcasting server is down not live
            connectionState.isServerDown()
            e.printStackTrace()
            return false
        }
        catch (e: IOException) {
            Log.d(TAG, "Can not establish connection to $SERVER_HOSTNAME:$SERVER_PORT")
            //Broadcasting server faces unknown issue
            connectionState.serverHasUnknownMatters()
            e.printStackTrace()
            return false
        }
        return true
    }
}