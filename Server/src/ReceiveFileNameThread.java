/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 18214304
 */
public class ReceiveFileNameThread extends Thread {

    String username;
    String fileName;
    DataInputStream in;

    /**
     *
     * @param username
     * @param fileName
     * @param in
     */
    public ReceiveFileNameThread(String username, String fileName, DataInputStream in) {
        this.username = username;
        this.fileName = fileName;
        this.in = in;
        
    }

    @Override
    public void run() {
        Server.bcFileRequest(username, fileName);
        
//        for (int i = 0; i < Server.listOfUsers.size() - 1; i++) {
        for (String usr : Server.listOfUsers.keySet()) {
            try {
                if (!usr.equals(username)){
                    System.out.println("IM IN THE RUN");
                
                    DataInputStream in2 = Server.listOfUsers.get(usr).in;
                    
                    System.out.println("IN2 :: "+ (in2==Server.listOfUsers.get(usr).in));
                    System.out.println("IN1 :: "+in);
                
                    String userFrom = in2.readUTF();
                    String fileNameRecv = in2.readUTF();
                
                    System.out.println("KAKAKAKAKAK");
                    
                    System.out.println("userFrom : "+userFrom);
                    System.out.println("fileNameRecv : "+fileNameRecv);
                
                    if (!fileNameRecv.equals("+")) {
                        Server.fileNames.get(username).put(userFrom, fileNameRecv);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ReceiveFileNameThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
