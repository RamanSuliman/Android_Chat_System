package raman.chatSystem.serverSide;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

// Accepts client connections, creates client threads to handle them and starts these threads

public class Server
{
    ServerSocket server = null;

    public Server()
    {
        init_server();
        accept_client_requests();
    }

    public static void main(String[] args) {
        new Server();
    }

    private void init_server()
    {
        int LISTENING_PORT = 2022;
        try {
            server = new ServerSocket(LISTENING_PORT);
            //Set timeout for a client trying to connect, this will help in releasing the accept() block for next connection.
            //server.setSoTimeout(4000);
            System.out.println("Server started on port " + LISTENING_PORT);
        }
        catch (IOException e) {
            System.err.println("Can not start listening on port " + LISTENING_PORT);
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /*
     * Accept and handle client connections
     */
    private void accept_client_requests()
    {
        ServerController serverDispatcher = new ServerController();
        Socket client = null;
        while(true)
        {
            try {
                client = server.accept();

                LiveUser clientInfo = new LiveUser();
                clientInfo.socket = client;

                ClientListener clientListener = new ClientListener(clientInfo, serverDispatcher);
                ClientSender clientSender = new ClientSender(clientInfo, serverDispatcher);

                clientInfo.clientListener = clientListener;
                clientInfo.clientSender = clientSender;

                clientListener.start();
                clientSender.start();

                serverDispatcher.addClient(clientInfo);
            }catch (SocketTimeoutException e) {
                assert client != null;
                System.err.println("Timeout, client " + client.getInetAddress() + " has failed to connect");
                e.printStackTrace();
                System.exit(-1);
            }
            catch (IOException e) {
                System.err.println("Could not accept new client... ");
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}

