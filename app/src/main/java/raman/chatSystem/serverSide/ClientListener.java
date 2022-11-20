package raman.chatSystem.serverSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ClientListener extends Thread
{
    private final ServerController serverController;
    private final LiveUser client;
    private final BufferedReader reader;

    public ClientListener(LiveUser client, ServerController serverController) throws IOException
    {
        this.client = client;
        this.serverController = serverController;
        reader = new BufferedReader(new InputStreamReader(client.socket.getInputStream()));
    }

    /*
     * Until interrupted, reads messages from the client socket, forwards them
     * to the server dispatcher's queue and notifies the server dispatcher.
     */
    @Override
    public void run()
    {
        try {
            client.clientName = reader.readLine();
            System.out.println(client.clientName + ": " + client.socket.getInetAddress() + " has joined the server! \t Number of users: " + serverController.num_active_users());
            while(!isInterrupted())
            {
                String message = reader.readLine();
                if(message == null)
                    break;
                //Holding user name and the message sent from
                serverController.distributeMessage(client, message);
            }
        } catch (IOException e) { // Problem reading from socket (communication is broken)
            System.out.println(client.clientName + " has left the chat");
        }

        // Communication is broken. Interrupt both listener and sender threads
        client.clientSender.interrupt();
        serverController.deleteClient(client);
    }
}