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

/**
 *
 * @author 18214304
 */
public class ReceiverThread extends Thread{
    private int NUM_OF_BLOCKS = 50;
    
    String filename;
    private int portNum = Client.portNum;
    private int filesize;
    private int blocksize;
    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    /**
     *
     */
    public static InputStream inFromSender;

    /**
     *
     */
    public static DataInputStream in;
    
    /**
     *
     * @param filename
     */
    public ReceiverThread(String filename) {
        this.filename = filename;
    }
    
    public void run(){
            System.out.println("PORTNUM ::: "+portNum);
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
                
                while(Client.isPaused){System.out.print("");}
                byte[] byteArray = new byte[blocksize];
                in.readFully(byteArray, 0, byteArray.length);
                bos.write(byteArray, 0, byteArray.length);
                tempCount -= blocksize;
                
                Client.chat.progressTheDownloadBar((int) 100 * i/50);
            }
            
            
            byte[] byteArray = new byte[tempCount];
            in.readFully(byteArray, 0, byteArray.length);
            bos.write(byteArray, 0, byteArray.length);
            bos.flush();
            
            Client.chat.progressTheDownloadBar(100);
            
            System.out.println("DONE WITH FILE :: ");
            
            inFromSender.close();
            in.close();
            clientSocket.close();
            serverSocket.close();
            
        } catch (Exception ex) {
            Logger.getLogger(ReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
