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
    private int portNum = Client.portNum;
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
            System.out.println("PORT NUUUMBER ::: "+portNum);
            
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
                while(Client.isPaused){}
                byte[] byteArray = new byte[blocksize];
                in.readFully(byteArray, 0, byteArray.length);
                bos.write(byteArray, 0, byteArray.length);
                tempCount -= blocksize;
                
                Client.chat.progressTheDownloadBar((int) 100 * i/50);
//                    for (int j = 0; j < byteArray.length; j++) {
//                        System.out.print(byteArray[j]);
//                    }
//                    System.out.println("");
            }
            
            System.out.println("TEMP COUNT :: "+tempCount);
            
            byte[] byteArray = new byte[tempCount];
            in.readFully(byteArray, 0, byteArray.length);
            bos.write(byteArray, 0, byteArray.length);
            bos.flush();
            
            System.out.println("DONE WITH FILE :: ");
            
            
        } catch (Exception ex) {
            Logger.getLogger(ReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
