
import java.io.BufferedReader;
import java.io.InputStream;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Acts as the main class for the server
 * 
 * @author 18214304 & 20059884
 */
public class Server extends Thread {

    /**
     * this private key
     */
    public static PrivateKey myPrivateKey;

    /**
     * this public key
     */
    public static PublicKey myPublicKey;

    /**
     * port number for sender
     */
    public static int portNumSender = 7998;

    /**
     * port number for receiver
     */
    public static int portNumReceiver = 7998;

    /**
     * output stream
     */
    public static OutputStream outFromServer;

    /**
     * object output stream
     */
    public static ObjectOutputStream out;

    /**
     * input stream
     */
    public static InputStream inFromClient;

    /**
     * object input stream
     */
    public static ObjectInputStream in;
    private static InputStream terminalIn = null;
    private static BufferedReader br = null;

    /**
     * map of users
     */
    public static ConcurrentHashMap<String, SocketHandler> listOfUsers = new ConcurrentHashMap<>();

    /**
     * file names map
     */
    public static ConcurrentHashMap<String, ConcurrentHashMap<String, String>> fileNames = new ConcurrentHashMap<>();

    /**
     * main method
     * 
     * @param args the arguments
     */
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
            PublicKey clientKey = null;
            
            try {
                clientSocket = serverSocket.accept();

                outFromServer = clientSocket.getOutputStream();
                out = new ObjectOutputStream(outFromServer);
                out.flush();
                inFromClient = clientSocket.getInputStream();
                in = new ObjectInputStream(inFromClient);

                out.writeObject(myPublicKey);
                out.flush();
                out.writeObject("");
                out.flush();

                clientKey = (PublicKey) in.readObject();
                String username = (String) in.readObject();
                System.out.println("Welcome: " + username + " to the chat");

                sh = new SocketHandler(clientKey, username, clientSocket, out, in);

                Thread t = new Thread(sh);
                t.start();

                listOfUsers.put(username, sh);
            } catch (Exception e) {
                System.err.println(e);
            }

            ClientConnecter connector = new ClientConnecter(serverSocket, clientSocket);
            connector.start();

            try {
                String userList = getListOfUsers();
                String msg = "&" + userList;
                out.writeObject(encrypt(clientKey, msg.getBytes()));
                out.flush();
            } catch (Exception e) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
            }

        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * gets the list of users
     * 
     * @return the list of users
     */
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

    /**
     * sends the user list
     * 
     * @param userList the user list
     */
    public static void sendUserList(String userList) {
        ObjectOutputStream out = null;

        for (Map.Entry<String, SocketHandler> pair : listOfUsers.entrySet()) {
            try {
                out = pair.getValue().getOut();
                PublicKey clientKey = pair.getValue().getClientKey();
                String msg = "&" + userList;
                out.writeObject(encrypt(clientKey, msg.getBytes()));
                out.flush();
            } catch (Exception e) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * broadcasts a message
     * 
     * @param username username of sender
     * @param message the message to be broadcast
     */
    public static void broadcast(String username, String message) {
        ObjectOutputStream out = null;

        for (Map.Entry<String, SocketHandler> pair : listOfUsers.entrySet()) {
            try {
                out = pair.getValue().getOut();
                PublicKey clientKey = pair.getValue().getClientKey();
                out.writeObject(encrypt(clientKey, username.getBytes()));
                out.flush();
                out.writeObject(encrypt(clientKey, message.getBytes()));
                out.flush();
            } catch (Exception e) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * broadcasts a file request
     * 
     * @param username username of requester
     * @param searchString file name searched
     */
    public static void bcFileRequest(String username, String searchString) {
        ObjectOutputStream out = null;

        for (Map.Entry<String, SocketHandler> pair : listOfUsers.entrySet()) {
            try {
                if (!pair.getKey().equals(username)) {
                    out = pair.getValue().getOut();
                    PublicKey clientKey = pair.getValue().getClientKey();
                    out.writeObject(encrypt(clientKey, "~".getBytes()));
                    out.flush();
                    out.writeObject(encrypt(clientKey, searchString.getBytes()));
                    out.flush();
                    out.writeObject(encrypt(clientKey, username.getBytes()));
                    out.flush();
                }
            } catch (Exception e) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * whispers a message to a user
     * 
     * @param usernameFrom username of sender
     * @param usernameTo username of receiver
     * @param message the message whispered
     */
    public static void whisper(String usernameFrom, String usernameTo, String message) {
        ObjectOutputStream out = null;

        for (Map.Entry<String, SocketHandler> pair : listOfUsers.entrySet()) {
            if (pair.getKey().equals(usernameTo) || pair.getKey().equals(usernameFrom)) {
                try {
                    out = pair.getValue().getOut();
                    PublicKey clientKey = pair.getValue().getClientKey();
                    String msg = usernameFrom + " > " + usernameTo;
                    out.writeObject(encrypt(clientKey, msg.getBytes()));
                    out.flush();
                    out.writeObject(encrypt(clientKey, message.getBytes()));
                    out.flush();
                } catch (Exception e) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    /**
     * sends the file list
     * 
     * @param usernameTo username of requester
     * @param fileList the file list
     */
    public static void sendFileList(String usernameTo, String fileList) {
        ObjectOutputStream out = null;

        for (Map.Entry<String, SocketHandler> pair : listOfUsers.entrySet()) {
            if (pair.getKey().equals(usernameTo)) {
                try {
                    out = pair.getValue().getOut();
                    PublicKey clientKey = pair.getValue().getClientKey();
                    out.writeObject(encrypt(clientKey, "$".getBytes()));
                    out.flush();
                    out.writeObject(encrypt(clientKey, fileList.getBytes()));
                    out.flush();
                } catch (Exception e) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    /**
     * sends message to chosen sender
     * 
     * @param filename requested filename
     * @param sender the sender
     * @param receiverIP receiver's ip address
     * @param newPort the new port to be listened on
     */
    public static void sendToSender(String filename, String sender, String receiverIP, int newPort) {
        ObjectOutputStream out = null;

        for (Map.Entry<String, SocketHandler> pair : listOfUsers.entrySet()) {
            if (pair.getKey().equals(sender)) {
                try {
                    out = pair.getValue().getOut();
                    PublicKey clientKey = pair.getValue().getClientKey();
                    out.writeObject(encrypt(clientKey, "-".getBytes()));
                    out.flush();
                    out.writeObject(encrypt(clientKey, filename.getBytes()));
                    out.flush();
                    out.writeObject(encrypt(clientKey, receiverIP.getBytes()));
                    out.flush();
                    out.writeObject(encrypt(clientKey, (newPort + "").getBytes()));
                    out.flush();
                    portNumSender--;
                } catch (Exception e) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    /**
     * sends the port number
     * 
     * @param usernameTo the username of the receiver
     */
    public static void sendPortNumber(String usernameTo) {
        ObjectOutputStream out = null;

        for (Map.Entry<String, SocketHandler> pair : listOfUsers.entrySet()) {
            if (pair.getKey().equals(usernameTo)) {
                try {
                    out = pair.getValue().getOut();
                    PublicKey clientKey = pair.getValue().getClientKey();
                    out.writeObject(encrypt(clientKey, (portNumReceiver + "").getBytes("-")));
                    out.flush();
                    portNumReceiver--;
                } catch (Exception e) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    /**
     * generate a keypair
     * 
     * @return a keypair
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator k = KeyPairGenerator.getInstance("RSA");
        k.initialize(2048);
        return k.genKeyPair();
    }

    /**
     * encrypts a message
     * 
     * @param key key for encryption
     * @param toEncrypt message to be encrypted
     * @return the encrypted message
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] encrypt(PublicKey key, byte[] toEncrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.ENCRYPT_MODE, key);
        return c.doFinal(toEncrypt);
    }

    /**
     * decrypts a message
     * 
     * @param toDecrypt the message to be decrypted
     * @return the decrypted message
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static byte[] decrypt(byte[] toDecrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.DECRYPT_MODE, myPrivateKey);
        return c.doFinal(toDecrypt);
    }
}
