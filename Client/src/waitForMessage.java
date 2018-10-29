
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.lang.Object;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class waitForMessage extends Thread {

    private static char UPDATE_USERS_SIGNAL = '&';

    ChatInterface chat = null;

    public waitForMessage(ChatInterface chat) {
        this.chat = chat;

    }

    @Override
    public void run() {

        try {
            waitForMsg(chat);
        } catch (IOException ex) {
            ChatInterface.connected = false;
            JOptionPane.showMessageDialog(chat, "You are disconnected from the server.");
            Logger.getLogger(waitForMessage.class.getName()).log(Level.SEVERE, null, ex);

        } catch (InterruptedException ex) {
            Logger.getLogger(waitForMessage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(waitForMessage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //An infinite while loop the is looking for incoming messages
    //It checks the code of the message and based on that categorizes the follwoing message
    public static void waitForMsg(ChatInterface chat) throws IOException, InterruptedException, ClassNotFoundException {
        String list_of_users = Client.receiveMsg();
        chat.addAllusers(list_of_users.substring(1, list_of_users.length()));
        
        while (true) {
            try {
                String anything = Client.receiveMsg();
                switch (anything.charAt(0)) {
                    case '&'://user list
                        String connectedUsr = anything.substring(1, anything.length());
                        chat.addAllusers(connectedUsr);
                        break;
                    case '#'://
                        String disconnectedUsr = Client.receiveMsg();
                        String list_of = Client.receiveMsg().substring(1);
                        chat.removeUsers(disconnectedUsr, list_of);
                        break;
                    case '~'://
                        System.out.println("~~~~~~~~~~~~~~~~");
                        String search = Client.receiveMsg();
                        String userFrom = Client.receiveMsg();
                        String fileNameFound = lookForFile(search);
                        System.out.println("Search : " + search );
                        System.out.println("Chosen File : " + fileNameFound);
                        byte[] l = Client.encrypt(Client.serverKey, "$".getBytes());
                        Client.out.writeObject(l);
                        Client.out.flush();
                        //Client.out.writeObject(chat.username);
                        //Client.out.flush();
                        byte[] e = Client.encrypt(Client.serverKey, chat.username.getBytes());
                        Client.out.writeObject(e);
                        Client.out.flush();
                        byte[] f = Client.encrypt(Client.serverKey, userFrom.getBytes());
                        Client.out.writeObject(f);
                        Client.out.flush();
                        byte[] g = Client.encrypt(Client.serverKey, fileNameFound.getBytes());
                        Client.out.writeObject(g);
                        Client.out.flush();
                        
                        break;
                    case '-'://
                        System.out.println("---------------");
                        
                        String filename = Client.receiveMsg();
                        String receiverIP = Client.receiveMsg();
                        int tempPort = Integer.parseInt(Client.receiveMsg());
                        Client.portNum = tempPort;
                        receiverIP = receiverIP.substring(1, receiverIP.indexOf(':'));
                        System.out.println("portNumber : "+ Client.portNum+ " Filename : " + filename + "   and   receiverIP :  "+ receiverIP);
                        SenderThread senderThread = new SenderThread(filename , receiverIP,tempPort);
                        senderThread.start();
                        break;
                    case '$'://
                        System.out.println("$$$$$$$$$$$$$$$$$$");
                        
                        String fileNames = Client.receiveMsg();
                        //Client.portNum = Integer.parseInt(Client.receiveMsg());
                        
                        boolean isPlusses = true;
                        if(fileNames.length() != 0 ){
                            String[] fileNameList = fileNames.split(",");
                            chat.filechooseDropDown.removeAll();
                                for (String s : fileNameList) {
                                    if((!s.equals("+"))&&(s.charAt(0) != '.')){
                                        isPlusses = false;
                                        chat.filechooseDropDown.addItem(s);
                                    }
                                }
                        }
                        if(isPlusses||fileNames.length()==0){
                            JOptionPane.showMessageDialog(chat, "No file found");
                        }
                        //convert and display
                        break;
                    default:
                        String who = anything;
                        String message = Client.receiveMsg();
                        chat.printMsg(message, who);
                        break;
                }
            } catch (Exception ex) {
                Logger.getLogger(waitForMessage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static String lookForFile(String search) {
        String filename = "";
        String cwd = System.getProperty("user.dir");
        File path = new File(cwd);
        double highScore = 0.3;
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {

            if (files[i].isFile()) {
                double score = similarity(files[i].getName(), search);
                if (score > highScore) {
                    filename = files[i].getName();
                    highScore = score;
                }
            }
        }
        if (filename.equals("")) {
            filename = "+";
        }
        return filename;
    }

    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
            /* both strings are zero length */ }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else if (j > 0) {
                    int newValue = costs[j - 1];
                    if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                        newValue = Math.min(Math.min(newValue, lastValue),
                                costs[j]) + 1;
                    }
                    costs[j - 1] = lastValue;
                    lastValue = newValue;
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }
}
