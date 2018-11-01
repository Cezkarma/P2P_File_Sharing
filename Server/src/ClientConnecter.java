
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;

/**
 * Connects the clients
 * 
 * @author 18214304 & 20059884
 */
public class ClientConnecter extends Thread {

    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    OutputStream outFromServer;
    ObjectOutputStream out;
    InputStream inFromClient;
    ObjectInputStream in;

    /**
     * the constructor for the client connector
     * 
     * @param serverSocket the server's socket
     * @param clientSocket the client's socket
     */
    public ClientConnecter(ServerSocket serverSocket, Socket clientSocket) {
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                clientSocket = serverSocket.accept();

                outFromServer = clientSocket.getOutputStream();
                out = new ObjectOutputStream(outFromServer);
                out.flush();
                inFromClient = clientSocket.getInputStream();
                in = new ObjectInputStream(inFromClient);

                out.writeObject(Server.myPublicKey);
                out.flush();

                out.writeObject(Server.getListOfUsers());
                out.flush();

                PublicKey clientKey = (PublicKey) in.readObject();
                String username = (String) in.readObject();

                SocketHandler sh = new SocketHandler(clientKey, username, clientSocket, out, in);
                Thread t = new Thread(sh);
                t.start();

                Server.listOfUsers.put(username, sh);

                System.out.println("Welcome: " + username + " to the chat");

                String userList = Server.getListOfUsers();
                Server.sendUserList(userList);
            } catch (Exception e) {
                System.err.println("SERVER2 " + e);
            }
        }
    }
}
