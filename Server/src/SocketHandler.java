
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The socket handler that handles messages to and from clients
 * 
 * @author 18214304 & 20059884
 */
public class SocketHandler implements Runnable {

    /**
     * determines whether or not to keep receiving filename
     */
    public static boolean ContinueReceivingFilename = true;
    private String DISCONNECT_MSG = "@";
    private String BROADCAST_MSG = "All";
    private String SEARCH_MSG = "~";
    private String FOUND_FILES = "$";
    private String FILE_CHOSEN = "*";
    private String WHO_DISCONNECTED_SGNL = "#";
    private String username;
    private Socket clientSocket;
    private PublicKey clientKey;
    ObjectInputStream in;
    ObjectOutputStream out;

    /**
     * constructor for sockethandler
     * @param clientKey the client key
     * @param username the username
     * @param clientSocket the client socket
     * @param out the output stream
     * @param in the input stream
     */
    public SocketHandler(PublicKey clientKey, String username, Socket clientSocket, ObjectOutputStream out, ObjectInputStream in) {
        this.clientKey = clientKey;
        this.username = username;
        this.clientSocket = clientSocket;
        this.out = out;
        this.in = in;

        Server.fileNames.put(username, new ConcurrentHashMap<String, String>());
    }

    @Override
    public void run() {
        System.out.println("new client thread created");

        while (true) {
            try {
                String toUser = new String(Server.decrypt((byte[]) in.readObject()));
                System.out.println("toUser : " + toUser);
                String message = new String(Server.decrypt((byte[]) in.readObject()));
                System.out.println("message : " + message);

                if (toUser.equals(BROADCAST_MSG)) {
                    Server.broadcast(username, message);
                } else if (toUser.equals(DISCONNECT_MSG)) {
                    Server.listOfUsers.remove(message);

                    if (!Server.listOfUsers.isEmpty()) {
                        Server.broadcast(WHO_DISCONNECTED_SGNL, message); //who disconnected
                        Server.sendUserList(Server.getListOfUsers());
                    } else {
                        System.out.println("No more users connected");
                    }
                } else if (toUser.equals(SEARCH_MSG)) {
                    Server.bcFileRequest(username, message);
                    Server.fileNames.get(username).clear();

                    while (Server.fileNames.get(username).size() < Server.listOfUsers.size() - 1) {
                    }

                    String fileNamesToSend = hashToString(Server.fileNames.get(username));
                    Server.sendFileList(username, fileNamesToSend);
                } else if (toUser.equals(FOUND_FILES)) {
                    String userFrom = message;
                    String userTo = new String(Server.decrypt((byte[]) in.readObject()));
                    String fileNameRecv = new String(Server.decrypt((byte[]) in.readObject()));

                    if (Server.fileNames.get(userTo).containsKey(fileNameRecv)) {
                        fileNameRecv = "." + fileNameRecv;
                    }

                    Server.fileNames.get(userTo).put(fileNameRecv, userFrom);
                    System.out.println("filenamerecv : " + Server.fileNames.get(userTo) + "     " + userTo + "   " + userFrom + "  " + Server.fileNames.get(userTo).size());
                } else if (toUser.equals(FILE_CHOSEN)) {
                    String fileSelected = message;
                    int tempPort1 = Integer.parseInt(new String(Server.decrypt((byte[]) in.readObject())));
                    int tempPort = Integer.parseInt(new String(Server.decrypt((byte[]) in.readObject())));
                    String userChosen = Server.fileNames.get(username).get(fileSelected);
                    Server.sendToSender(fileSelected, userChosen, clientSocket.getRemoteSocketAddress().toString(), tempPort);
                    Server.fileNames.get(username).clear();
                } else {
                    Server.whisper(username, toUser, message);
                }
            } catch (EOFException e) {
                try {
                    System.out.println("Client " + username + " has disconnected");
                    clientSocket.close();
                    break;
                } catch (IOException ex) {
                    Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
                
                try {
                    clientSocket.close();
                } catch (Exception e) {
                    Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println("could not disconnect " + e);
                }

                break;
            } catch (Exception ex) {
                Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * gets the client socket
     * 
     * @return the client socket
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * gets output stream
     * 
     * @return the output stream
     */
    public ObjectOutputStream getOut() {
        return out;
    }

    /**
     * gets the client key
     * 
     * @return the client key
     */
    public PublicKey getClientKey() {
        return clientKey;
    }

    /**
     * converts hashmap entries to a string
     * 
     * @param map the hashmap
     * @return hashmap entries in string form
     */
    public static String hashToString(ConcurrentHashMap<String, String> map) {
        String toSend = "";

        for (Map.Entry<String, String> pair : map.entrySet()) {
            toSend = toSend + pair.getKey() + ",";
        }

        if (toSend.length() > 0) {
            return toSend.substring(0, toSend.length() - 1);
        } else {
            return "";
        }
    }
}
