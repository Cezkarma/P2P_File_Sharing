//package rw354_tut1_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
//import static rw354_tut1_server.Server.inFromClient;
//import static rw354_tut1_server.Server.outFromServer;

public class ClientConnecter extends Thread {
    
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    OutputStream outFromServer;
    DataOutputStream out;
    InputStream inFromClient;
    DataInputStream in;
    
    public ClientConnecter (ServerSocket serverSocket, Socket clientSocket) {
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                clientSocket = serverSocket.accept();
                
                inFromClient = clientSocket.getInputStream();
                in = new DataInputStream(inFromClient);
                outFromServer = clientSocket.getOutputStream();
                out = new DataOutputStream(outFromServer);

                out.writeUTF(Server.getListOfUsers());
                String username = in.readUTF();
                
                SocketHandler sh = new SocketHandler(username, clientSocket);
                Thread t = new Thread(sh);
                t.start();
                
                Server.listOfUsers.put(username, sh);
                
                System.out.println("Welcome: "+username+" to the chat");
                
                String userList = Server.getListOfUsers();
                Server.sendUserList(userList);                 
            } catch (Exception e) {
                System.err.println("SERVER2 "+e);
            }
            
            
        }
    }
    
    
    
    
}
