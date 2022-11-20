package raman.chatSystem.serverSide;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

public class ClientSender extends Thread
{
    private final Vector<String> messageQueue = new Vector<>();
    private final ServerController serverController;
    private final LiveUser client;
    private final PrintWriter writer;

    public ClientSender(LiveUser client, ServerController serverController) throws IOException
    {
        this.serverController = serverController;
        this.client = client;
        writer = new PrintWriter(client.socket.getOutputStream(), true);
    }

    /*
     * Adds given message to the message queue and notifies this thread
     * (actually getNextMessageFromQueue method) that a message is arrived.
     *  sendMessage is called by other threads (ServeDispatcher).
     */
    public synchronized void sendMessage(String message) {
        messageQueue.add(message);
        notify();
    }

    /**
     * @return and deletes the next message from the message queue. If the queue
     * is empty, falls in sleep until notified for message arrival by sendMessage
     * method.
     */
    private synchronized String getNextMessageFromQueue() throws InterruptedException
    {
        while(messageQueue.size() == 0)
            wait();
        String message = messageQueue.get(0);
        messageQueue.removeElementAt(0);
        return message;
    }

    /**
     * Sends given message to the client's socket.
     */
    private void sendMessageToClient(String message)
    {
        writer.println(message);
    }

    public void run()
    {
        sendMessage("Message from server!!!!!!");
        try {
            while(!isInterrupted())
            {

                String message = getNextMessageFromQueue();
                sendMessageToClient(message);
            }
        } catch (Exception e) {System.out.println("Client lost connection... ");}

        // Communication is broken. Interrupt both listener and sender threads
        client.clientSender.interrupt();
        serverController.deleteClient(client);
    }
}

