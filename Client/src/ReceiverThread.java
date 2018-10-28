import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverThread extends Thread{
    private int NUM_OF_BLOCKS = 50;
    
    String filename;
    private int portNum = 7998;//= Client.portNum;
    private int filesize;
    private int blocksize;
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    public static InputStream inFromSender;
    public static DataInputStream in;
    
    public ReceiverThread(String filename) {
        this.filename = filename;
    }
    
    public void run(){
        try {
            String cwd = System.getProperty("user.dir");
            File file = new File(cwd+"/"+filename);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            
            serverSocket = new ServerSocket(portNum);
            clientSocket = serverSocket.accept();
            
            inFromSender = clientSocket.getInputStream();
            in = new DataInputStream(inFromSender);
            
            filesize = in.readInt();
            blocksize = in.readInt();
            
            
            byte[] b = new byte[filesize];
            
            int tempCount = filesize;
            
            for (int i = 0; i < NUM_OF_BLOCKS - 1; i++) {
                byte[] byteArray = new byte[blocksize];
                in.readFully(byteArray, 0, byteArray.length);
                bos.write(byteArray, 0, byteArray.length);
                tempCount -= blocksize;
            }
            
            byte[] byteArray = new byte[tempCount];
            in.readFully(byteArray, 0, byteArray.length);
            bos.write(byteArray, 0, byteArray.length);
            bos.flush();
            
        } catch (Exception ex) {
            Logger.getLogger(ReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
