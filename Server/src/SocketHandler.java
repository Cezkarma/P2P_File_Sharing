//package rw354_tut1_server;

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

public class SocketHandler implements Runnable {

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
                    //send port nr to receiver

                    Server.bcFileRequest(username, message);

                    //Server.portNum--;
                    Server.fileNames.get(username).clear();

                    while (Server.fileNames.get(username).size() < Server.listOfUsers.size() - 1) {
                    }
                    //System.out.println(Server.fileNames.get(username).size());System.out.println(Server.listOfUsers.size());

                    System.out.println("jjj");

                    String fileNamesToSend = hashToString(Server.fileNames.get(username));
                    Server.sendFileList(username, fileNamesToSend);
                    //Server.sendPortNumber(username);

                    //Server.portNum--;
                } else if (toUser.equals(FOUND_FILES)) {
                    String userFrom = message;
                    String userTo = new String(Server.decrypt((byte[]) in.readObject()));
//                    userTo = new String(Server.decrypt(userTo.getBytes()));
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

                    System.out.println("file selected : " + fileSelected);
                    System.out.println("user selected : " + userChosen);
                    System.out.println("port : " + tempPort);

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
                //bc that user disconnected
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

    public Socket getClientSocket() {
        return clientSocket;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public PublicKey getClientKey() {
        return clientKey;
    }

    public static String hashToString(ConcurrentHashMap<String, String> map) {
        String toSend = "";
        for (Map.Entry<String, String> pair : map.entrySet()) {
            toSend = toSend + pair.getKey() + ",";
        }

        System.out.println("TO SEND ::: " + toSend);

        if (toSend.length() > 0) {
            return toSend.substring(0, toSend.length() - 1);
        } else {
            return "";
        }
    }
}
