package raman.chatSystem.serverSide;

import java.net.Socket;

public class LiveUser
{
    public Socket socket = null;
    public ClientListener clientListener = null;
    public ClientSender clientSender = null;
    public String clientName = "None";
}

