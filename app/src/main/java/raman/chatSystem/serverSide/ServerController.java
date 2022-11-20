package raman.chatSystem.serverSide;

import java.util.Vector;


/*
 * Waits for a messages and sends arrived messages to all the clients connected to the server
 */
public class ServerController extends Thread
{
    private final Vector<String> messageQueue = new Vector<>();
    private final Vector<LiveUser> clients = new Vector<>();
    private LiveUser messgaeReceviedFromClient = null;

    public ServerController()
    {
        start();
    }

    /**
     * Infinitely reads messages from the queue and dispatch them
     * to all clients connected to the server.
     */
    @Override
    public void run()
    {
        try {
            while(!isInterrupted())
            {
                String message = getNextMessageFromQueue();
                sendMessageToAllClients(message);
            }
        } catch (InterruptedException ie)
        {
            ie.printStackTrace();
            // Thread interrupted. Stop its execution
        }
    }

    /**
     * Sends given message to all clients in the client list. Actually the
     * message is added to the client sender thread's message queue and this
     * client sender thread is notified.
     */
    private synchronized void sendMessageToAllClients(String message)
    {
        for(LiveUser client : clients)
            if(messgaeReceviedFromClient != client)
                client.clientSender.sendMessage(message);
    }

    /**
     * @return and deletes the next message from the message queue. If there is no
     * messages in the queue, falls in sleep until notified by dispatchMessage method.
     */
    private synchronized String getNextMessageFromQueue() throws InterruptedException
    {
        while (messageQueue.size() == 0)
            wait();
        String message = messageQueue.get(0);
        messageQueue.removeElementAt(0);
        return message;
    }

    /*
     * Adds given message to the dispatcher's message queue and notifies this
     * thread to wake up the message queue reader (getNextMessageFromQueue method).
     * dispatchMessage method is called by other threads (ClientListener) when
     * a message is arrived.
     */
    public synchronized void distributeMessage(LiveUser client, String message)
    {
        //Attaching client name with the message before sending
        //message = client.clientName + " : " + message;
        //System.out.println(message);
        messageQueue.add(message);
        //Assign current user to send messages to all expect who it came from
        messgaeReceviedFromClient = client;
        notify();
    }

    /* Adds given client to the list */
    public synchronized void addClient(LiveUser client)
    {
        clients.add(client);
    }

    /* Deletes given client from the server's client list */
    public synchronized void deleteClient(LiveUser client)
    {
        int clientIndex = clients.indexOf(client);
        if(clientIndex != -1)
        {
            clients.removeElementAt(clientIndex);
            sendMessageToAllClients("\n" + client.clientName + " left...\n");
        }
    }

    public int num_active_users()
    {
        return clients.size();
    }
}

