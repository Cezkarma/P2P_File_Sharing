
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

public class Client {

    public static PrivateKey myPrivateKey;
    public static PublicKey myPublicKey;
    public static boolean isPaused = false;
    private static String DISCONNECT_MSG = "@";
    public static String fileName;
    public static int portNum = 7999;
    public boolean valid_connection = true;
    private final static int port = 8000;
    static String serverName = "146.232.50.162";
    static OutputStream outToServer;
    static DataOutputStream out;
    static InputStream inFromServer;
    static DataInputStream in;
    static Socket client;
    public static ChatInterface chat;
    public static String IP_ad;

    /**
     * @param args the command line arguments
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
    }

    //Connects the client sockect to the server socket. 
    //Receivees the list of currently connected users and sends username.
    // It calls method waitForMessage which starts a thread and conctantly looks for incoming messages
    public static void connect(String serverName, String usr) throws IOException {
        boolean validIP = false;
        try {
            IP_ad = chat.IP;
            client = new Socket(IP_ad, port);

            validIP = client.isConnected();
            if (!validIP) {
                JOptionPane.showMessageDialog(chat, "invalid IP");
                return;
            }
            String userList_intial = receiveMsg();
            boolean validUsrn = checkUsername(userList_intial, chat.username);
            if (!validUsrn) {
                JOptionPane.showMessageDialog(chat, "Username taken , new username : " + chat.username);
            }
            outToServer = client.getOutputStream();
            out = new DataOutputStream(outToServer);
            out.writeUTF(chat.username);
            waitForMessage waitFor = new waitForMessage(chat);

            waitFor.start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(chat, "Could not connect to server : " + e);
        }

    }

    public static String getIPaddr() {
        return IP_ad;
    }

    public static String getServerName() {
        return serverName;
    }

    public static void sendMessage(String msg, String usr) throws IOException {
        out.writeUTF(usr);
        out.writeUTF(msg);
    }

    // Disconnects the user : closes all dataStreams as well as the socket. It also notifies the Server beforehand
    public static void disconnect(String usr) {
        try {
            if (ChatInterface.connected) {
                out.writeUTF(DISCONNECT_MSG);
                out.writeUTF(usr);
                out.close();
                outToServer.close();
                in.close();
                inFromServer.close();
                client.close();
            }
            chat.dispose();
        } catch (IOException ex) {
            System.err.println("Disconnection Error : " + ex);
        }

    }

    public static String receiveMsg() throws IOException {
        inFromServer = client.getInputStream();
        in = new DataInputStream(inFromServer);
        String inputFromServer = in.readUTF();
        return inputFromServer;
    }

    //Runs through list of usernames and check if the current username is already take , if so it assigns a new username
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
