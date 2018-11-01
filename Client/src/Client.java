
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

/**
 *
 * @author 18214304
 */
public class Client {

    /**
     * The Client's private key
     */
    public static PrivateKey myPrivateKey;

    /**
     * The Client's public key
     */
    public static PublicKey myPublicKey;

    /**
     * Boolean that indicates if download is paused
     */
    public static boolean isPaused = false;
    private static String DISCONNECT_MSG = "@";

    /**
     * the filename that is to be sent
     */
    public static String fileName;

    /**
     * The prot number used
     */
    public static int portNum = 7999;

    /**
     * a Boolean that indicates the if the sockets connected succesfully
     */
    public boolean valid_connection = true;
    private final static int port = 8000;
    static String serverName = "146.232.50.162";
    static OutputStream outToServer;
    static ObjectOutputStream out;
    static InputStream inFromServer;
    static ObjectInputStream in;
    static Socket client;

    /**
     * The Gui
     */
    public static ChatInterface chat;

    /**
     * The IP of the server
     */
    public static String IP_ad;

    /**
     * The servers public key
     */
    public static PublicKey serverKey;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.security.NoSuchAlgorithmException
     */
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        // TODO code application logic here
        String message;
        String who;
        chat = new ChatInterface();
        chat.show();
        KeyPair keys = generateKeys();
        myPrivateKey = keys.getPrivate();
        myPublicKey = keys.getPublic();
        System.out.println("IN MAIN");
    }

    /**
     * Connects the client sockect to the server socket. Receivees the list of
     * currently connected users and sends username. It calls method
     * waitForMessage which starts a thread and conctantly looks for incoming
     * messages
     *
     * @param serverName the name of the server
     * @param usr the name of the user
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void connect(String serverName, String usr) throws IOException, ClassNotFoundException {
        boolean validIP = false;
        try {
            IP_ad = chat.IP;
            client = new Socket(IP_ad, port);

            validIP = client.isConnected();
            if (!validIP) {
                JOptionPane.showMessageDialog(chat, "invalid IP");
                return;
            }
            outToServer = client.getOutputStream();
            out = new ObjectOutputStream(outToServer);

            inFromServer = client.getInputStream();
            in = new ObjectInputStream(inFromServer);
            serverKey = receiveObj();

            String userList_intial = (String) in.readObject();
            System.out.println("Received initial user list" + userList_intial);
            out.writeObject(myPublicKey);
            out.flush();
            boolean validUsrn = checkUsername(userList_intial, chat.username);
            if (!validUsrn) {
                JOptionPane.showMessageDialog(chat, "Username taken , new username : " + chat.username);
            }
            System.out.println("myPublicKey  : " + myPublicKey.toString());
            out.writeObject(chat.username);
            out.flush();
            waitForMessage waitFor = new waitForMessage(chat);

            waitFor.start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(chat, "Could not connect to server : " + e);
        }

    }

    /**
     * Returns the IP of the server
     *
     * @return the servers IP
     */
    public static String getIPaddr() {
        return IP_ad;
    }

    /**
     * Returns the name of the server
     *
     * @return the name of the server
     */
    public static String getServerName() {
        return serverName;
    }

    /**
     * Sends two encrypted messages over to the server with objectStreams
     *
     * @param msg the message being send
     * @param usr the user that sends the message
     * @throws IOException
     */
    public static void sendMessage(String msg, String usr) throws IOException {
        try {
            byte[] l = encrypt(serverKey, usr.getBytes());
            out.writeObject(l);
            out.flush();
            byte[] k = encrypt(serverKey, msg.getBytes());
            out.writeObject(k);
            out.flush();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Disconnects the user : closes all dataStreams as well as the socket. It
     * also notifies the Server beforehand
     *
     * @param usr the user that disconnected
     */
    public static void disconnect(String usr) {
        try {
            if (ChatInterface.connected) {
                byte[] l = encrypt(serverKey, DISCONNECT_MSG.getBytes());
                out.writeObject(l);
                out.flush();
                byte[] k = encrypt(serverKey, usr.getBytes());
                out.writeObject(k);
                out.flush();
                out.close();
                outToServer.close();
                in.close();
                inFromServer.close();
                client.close();
            }
            chat.dispose();
        } catch (IOException ex) {
            System.err.println("Disconnection Error : " + ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Receives the servers public key
     *
     * @return servers key
     * @throws IOException
     */
    public static PublicKey receiveObj() throws IOException {
        PublicKey inputFromServer = null;
        try {
            inputFromServer = (PublicKey) in.readObject();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inputFromServer;
    }

    /**
     * Receive and decrypts a message from the server.
     *
     * @return the received message
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static String receiveMsg() throws IOException, ClassNotFoundException {
        byte[] temp = (byte[]) in.readObject();
        String inputFromServer = new String();
        try {
            inputFromServer = new String(decrypt(temp));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inputFromServer;
    }

    /**
     * Runs through list of usernames and check if the current username is
     * already take , if so it assigns a new username
     *
     * @param list the list of users to check for duplicates
     * @param usrnm the username selected
     * @return
     */
    public static boolean checkUsername(String list, String usrnm) {
        boolean valid = true;
        List<String> tempList = Arrays.asList(list.split(","));
        if (tempList.contains(usrnm)) {
            valid = false;
            double randomInt = (Math.random());
            chat.username = chat.username + (int) (randomInt * 1000);
        }

        return valid;
    }

    /**
     * Generates a public and private key
     *
     * @return a key pair
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator k = KeyPairGenerator.getInstance("RSA");
        k.initialize(2048);
        return k.genKeyPair();
    }

    /**
     * Encrypts a message with a given key
     *
     * @param key the encryption key
     * @param toEncrypt what to encrypt
     * @return
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
     * Decrypts the message with my private key
     *
     * @param toDecrypt what to decrypt
     * @return th decrypted message
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
