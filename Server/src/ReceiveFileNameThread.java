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

    public ReceiveFileNameThread(String username, String fileName, DataInputStream in) {
        this.username = username;
        this.fileName = fileName;
        this.in = in;
    }

    @Override
    public void run() {
        Server.bcFileRequest(username, fileName);
        for (int i = 0; i < Server.listOfUsers.size() - 1; i++) {
            try {
                String userFrom = in.readUTF();
                String fileNameRecv = in.readUTF();
                if (!fileNameRecv.equals("+")) {
                    Server.fileNames.get(username).put(userFrom, fileNameRecv);
                }
            } catch (IOException ex) {
                Logger.getLogger(ReceiveFileNameThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
