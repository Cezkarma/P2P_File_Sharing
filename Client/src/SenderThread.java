
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 18214304
 */
public class SenderThread extends Thread {
    public String filename;
    public String receiverIP;
    public static OutputStream outToReceiver;
    public static DataOutputStream out;
    Socket socket = null;
    public static int blockSize = 100;
    public static int numOfBlocks = 50;
    public static int port = Client.portNum;
    public SenderThread(String filename , String receiverIP) {
        this.filename = filename;
        this.receiverIP = receiverIP;   
    }
    
    @Override
    public void run() {
        try {
            System.out.println("port number :  "  + port);
            socket = new Socket(receiverIP , port );
            outToReceiver = socket.getOutputStream();
            out = new DataOutputStream(outToReceiver);
            String cwd = System.getProperty("user.dir");
            File file = new File(cwd+"/"+filename);
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            byte[] b = new byte[(int)raf.length()];
            //raf.readFully(b);
            blockSize = b.length/numOfBlocks;
            
            out.writeInt(b.length);
            System.out.println("b.length " + b.length );
            System.out.println("blocksize " + blockSize );

            out.writeInt(blockSize);
            
            
            int tempCount = b.length;
            
            try {
                for (int i = 0; i < numOfBlocks - 1; i++) {
                    byte[] byteArray = new byte[blockSize];
                    raf.read(byteArray);
                    out.write(byteArray, 0, byteArray.length);
                    out.flush();
                    tempCount -= blockSize;
//                    for(int j = 0 ; j < byteArray.length; j++){
//                        System.out.print(byteArray[j] );
//                    }
//                    System.out.println("");
                }

                System.out.println("temp count : " + tempCount);

                byte[] byteArray = new byte[tempCount];
                raf.read(byteArray);
                out.write(byteArray, 0, byteArray.length);
                out.flush();
                

            } catch (IOException ex) {
                Logger.getLogger(cwd).log(Level.SEVERE, null, ex);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SenderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
}
