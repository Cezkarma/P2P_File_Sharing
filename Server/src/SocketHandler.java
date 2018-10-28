//package rw354_tut1_server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
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
    InputStream inFromClient;
    DataInputStream in;

    public SocketHandler(String username, Socket clientSocket) {
        this.username = username;
        this.clientSocket = clientSocket;

        Server.fileNames.put(username, new ConcurrentHashMap<String, String>());
    }

    @Override
    public void run() {
        System.out.println("new client thread created");
        try {
            inFromClient = clientSocket.getInputStream();
            in = new DataInputStream(inFromClient);
        } catch (Exception e) {
            System.out.println("could not open stream for this client " + e);
        }

        while (true) {
            try {
                String toUser = in.readUTF();
                String message = in.readUTF();

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

                    in.close();
                    inFromClient.close();
                } else if (toUser.equals(SEARCH_MSG)) {
                    ReceiveFileNameThread t = new ReceiveFileNameThread(username, message, in);
                    t.start();
                    Thread.sleep(3000);
                    String fileNamesToSend = hashToString(Server.fileNames.get(username));
                    Server.sendFileList(username, fileNamesToSend);
                } else if (toUser.equals(FOUND_FILES)) {
                } else if (toUser.equals(FILE_CHOSEN)) {
                    Server.fileNames.clear();
                    //Form dirct connection
                } else {
                    Server.whisper(username, toUser, message);
                }
            } catch (IOException ex) {
                System.err.println(ex);
                //bc that user disconnected
                try {
                    clientSocket.close();
                } catch (Exception e) {
                    System.err.println("could not disconnect " + e);
                }

                break;
            } catch (InterruptedException ex) {
                Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public static String hashToString(ConcurrentHashMap<String, String> map) {
        String toSend = "";
        for (Map.Entry<String, String> pair : map.entrySet()) {
            toSend += pair.getKey() + ",";
        }
        return toSend.substring(0, toSend.length() - 1);
    }

}
