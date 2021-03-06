//package rw354_tut1_server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Server extends Thread {

    public static PrivateKey myPrivateKey;
    public static PublicKey myPublicKey;
    public static int portNumSender = 7998;
    public static int portNumReceiver = 7998;
    public static OutputStream outFromServer;
    public static DataOutputStream out;
    public static ObjectOutputStream objectOut;
    public static InputStream inFromClient;
    public static DataInputStream in;
    public static ObjectInputStream objectIn;
    private static InputStream terminalIn = null;
    private static BufferedReader br = null;
    public static ConcurrentHashMap<String, SocketHandler> listOfUsers = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, ConcurrentHashMap<String, String>> fileNames = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try {
            int portNumber = 8000;
            ServerSocket serverSocket = null;
            Socket clientSocket = null;
            KeyPair keys = generateKeys();
            myPrivateKey = keys.getPrivate();
            myPublicKey = keys.getPublic();
            
            try {
                serverSocket = new ServerSocket(portNumber);
                System.out.println(serverSocket);
            } catch (Exception e) {
                System.exit(0);
            }
            
            SocketHandler sh = null;
            try {
                clientSocket = serverSocket.accept();
                
                inFromClient = clientSocket.getInputStream();
                in = new DataInputStream(inFromClient);
                objectIn = new ObjectInputStream(inFromClient);
                outFromServer = clientSocket.getOutputStream();
                out = new DataOutputStream(outFromServer);
                objectOut = new ObjectOutputStream(outFromServer);
                
                objectOut.writeObject(myPublicKey);
                System.out.println("SRVR PKEY - "+myPublicKey.toString());
                
                out.writeUTF("");
                
                PublicKey clientKey = (PublicKey) objectIn.readObject();
                String username = in.readUTF();
                System.out.println("Welcome: " + username + " to the chat");
                
                sh = new SocketHandler(clientKey, username, clientSocket);
                
                Thread t = new Thread(sh);
                t.start();
                
                listOfUsers.put(username, sh);
            } catch (Exception e) {
                System.err.println(e);
            }
            
            ClientConnecter connector = new ClientConnecter(serverSocket, clientSocket);
            connector.start();
            
            try {
                
                outFromServer = clientSocket.getOutputStream();
                out = new DataOutputStream(outFromServer);
                
                String userList = getListOfUsers();
                out.writeUTF("&" + userList);
                
            } catch (Exception e) {
                System.err.println("SERVER: " + e);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String getListOfUsers() {
        String userList = "";

        if (!listOfUsers.isEmpty()) {
            for (String key : listOfUsers.keySet()) {
                userList = userList + key + ",";
            }
        }

        if (userList != "") {
            userList = userList.substring(0, userList.length() - 1);
        }

        return userList;
    }

    public static void sendUserList(String userList) {
        OutputStream outFromServer = null;
        DataOutputStream out = null;

        for (Map.Entry<String, SocketHandler> pair : listOfUsers.entrySet()) {
            try {
                outFromServer = pair.getValue().getClientSocket().getOutputStream();//.getClientSocket().getOutputStream();
                out = new DataOutputStream(outFromServer);

                out.writeUTF("&" + userList);

            } catch (Exception e) {
                System.err.println("problem in sendUserList " + e);
            }
        }

    }

    public static void broadcast(String username, String message) {
        OutputStream outFromServer = null;
        DataOutputStream out = null;

        for (Map.Entry<String, SocketHandler> pair : listOfUsers.entrySet()) {
            try {
                outFromServer = pair.getValue().getClientSocket().getOutputStream();//.getClientSocket().getOutputStream();
                out = new DataOutputStream(outFromServer);
                out.writeUTF(username);
                out.writeUTF(message);
            } catch (Exception e) {
                System.err.println("problem in broadcast " + e);
            }
        }
    }

    public static void bcFileRequest(String username, String searchString) {
        OutputStream outFromServer = null;
        DataOutputStream out = null;

        for (Map.Entry<String, SocketHandler> pair : listOfUsers.entrySet()) {
            try {
                if (!pair.getKey().equals(username)) {
                    outFromServer = pair.getValue().getClientSocket().getOutputStream();//.getClientSocket().getOutputStream();
                    out = new DataOutputStream(outFromServer);
                    out.writeUTF("~");
                    out.writeUTF(searchString);
                    out.writeUTF(username);
                }
            } catch (Exception e) {
                System.err.println("problem in broadcast " + e);
            }
        }
    }

    public static void whisper(String usernameFrom, String usernameTo, String message) {
        OutputStream outFromServer = null;
        DataOutputStream out = null;

        for (Map.Entry<String, SocketHandler> pair : listOfUsers.entrySet()) {
            if (pair.getKey().equals(usernameTo) || pair.getKey().equals(usernameFrom)) {
                try {
                    outFromServer = pair.getValue().getClientSocket().getOutputStream();//.getClientSocket().getOutputStream();
                    out = new DataOutputStream(outFromServer);
                    out.writeUTF(usernameFrom + " > " + usernameTo);
                    out.writeUTF(message);
                } catch (Exception e) {
                    System.err.println("could not whisper : " + e);
                }
            }
        }
    }

    public static void sendFileList(String usernameTo, String fileList) {
        OutputStream outFromServer = null;
        DataOutputStream out = null;

        System.out.println("1");

        for (Map.Entry<String, SocketHandler> pair : listOfUsers.entrySet()) {
            if (pair.getKey().equals(usernameTo)) {
                try {
                    outFromServer = pair.getValue().getClientSocket().getOutputStream();//.getClientSocket().getOutputStream();
                    out = new DataOutputStream(outFromServer);
                    out.writeUTF("$");
                    out.writeUTF(fileList);

                    System.out.println("2");
                } catch (Exception e) {
                    System.err.println("could not whisper : " + e);
                }
            }
        }
    }

    public static void sendToSender(String filename, String sender, String receiverIP, int newPort) {
        OutputStream outFromServer = null;
        DataOutputStream out = null;

        for (Map.Entry<String, SocketHandler> pair : listOfUsers.entrySet()) {
            if (pair.getKey().equals(sender)) {
                try {
                    outFromServer = pair.getValue().getClientSocket().getOutputStream();//.getClientSocket().getOutputStream();
                    out = new DataOutputStream(outFromServer);
                    out.writeUTF("-");
                    out.writeUTF(filename);
                    out.writeUTF(receiverIP);
                    out.writeUTF(newPort + "");
                    portNumSender--;
                    System.out.println("REACHED :: " + portNumSender);
                } catch (Exception e) {
                    System.err.println("could not whisper : " + e);
                }
            }
        }
    }

    public static void sendPortNumber(String usernameTo) {
        OutputStream outFromServer = null;
        DataOutputStream out = null;

        for (Map.Entry<String, SocketHandler> pair : listOfUsers.entrySet()) {
            if (pair.getKey().equals(usernameTo)) {
                try {
                    outFromServer = pair.getValue().getClientSocket().getOutputStream();//.getClientSocket().getOutputStream();
                    out = new DataOutputStream(outFromServer);
                    //portNum--;
                    out.writeUTF(portNumReceiver + "");
                    portNumReceiver--;
                } catch (Exception e) {
                    System.err.println("could not whisper : " + e);
                }
            }
        }
    }

    public static KeyPair generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator k = KeyPairGenerator.getInstance("RSA");
        k.initialize(2048);
        return k.genKeyPair();
    }

    public static byte[] encrypt(PublicKey key, byte[] toEncrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.ENCRYPT_MODE, key);
        return c.doFinal(toEncrypt);
    }

    public static byte[] decrypt(byte[] toDecrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.DECRYPT_MODE, myPrivateKey);
        return c.doFinal(toDecrypt);
    }
}
