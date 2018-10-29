/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package rw354_tut1_client;

import java.awt.Color;
import java.io.IOException;
//import static java.time.Clock.system;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
//import rw354_tut1_client.Client;
//import static javafx.application.Platform.exit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
//import static javax.swing.JOptionPane.YES_OPTION;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import static rw354_tut1_client.Client.chat;
//import static rw354_tut1_client.Client.connect;

/**
 *
 * @author rabbp
 */
public class ChatInterface extends javax.swing.JFrame {

    public static boolean connected = false;
    String username;
    String msg;
    String serverName = Client.getServerName();
    String IP;
    List<String> list = null;

    /**
     * Creates new form ChatInterface
     */
    public ChatInterface() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Chat_txt = new java.awt.TextArea();
        users_txt = new java.awt.TextArea();
        send_btn = new javax.swing.JButton();
        chat_choice_dropdown = new java.awt.Choice();
        username_txt = new javax.swing.JTextField();
        connect_btn = new javax.swing.JButton();
        disconnect_btn = new javax.swing.JButton();
        msg_txt = new javax.swing.JTextField();
        IP_addr = new javax.swing.JTextField();
        reset_btn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        searchText = new javax.swing.JTextArea();
        searchButton = new javax.swing.JButton();
        downloadProgBar = new javax.swing.JProgressBar();
        jLabel4 = new javax.swing.JLabel();
        uploadProgBar = new javax.swing.JProgressBar();
        jLabel5 = new javax.swing.JLabel();
        downloadPauseBtn = new javax.swing.JButton();
        downloadBtn = new javax.swing.JButton();
        filechooseDropDown = new java.awt.Choice();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        Chat_txt.setEditable(false);

        users_txt.setEditable(false);

        send_btn.setText("Send");
        send_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send_btnActionPerformed(evt);
            }
        });

        chat_choice_dropdown.add("All");

        username_txt.setText("Rabz");
        username_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                username_txtActionPerformed(evt);
            }
        });

        connect_btn.setText("Connect");
        connect_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connect_btnActionPerformed(evt);
            }
        });

        disconnect_btn.setText("Disconnect");
        disconnect_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnect_btnActionPerformed(evt);
            }
        });

        msg_txt.setText("Whatup");
        msg_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                msg_txtActionPerformed(evt);
            }
        });

        IP_addr.setText("146.232.50.153");
        IP_addr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IP_addrActionPerformed(evt);
            }
        });

        reset_btn.setText("RESET");
        reset_btn.setEnabled(false);
        reset_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reset_btnActionPerformed(evt);
            }
        });

        jLabel1.setText("If server Disconnects , hit reset");

        jLabel2.setText("CHAT AREA:");

        jLabel3.setText("FILE SELECT AREA:");

        searchText.setColumns(20);
        searchText.setRows(5);
        searchText.setText("archer");
        jScrollPane1.setViewportView(searchText);

        searchButton.setText("SEARCH");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Download Progress:");

        jLabel5.setText("Upload Progress:");

        downloadPauseBtn.setText("Pause");
        downloadPauseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadPauseBtnActionPerformed(evt);
            }
        });

        downloadBtn.setText("DOWNLOAD");
        downloadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(username_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(IP_addr, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(connect_btn)
                                .addGap(54, 54, 54)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(users_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(disconnect_btn)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(Chat_txt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(msg_txt)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(chat_choice_dropdown, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(send_btn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(searchButton)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(38, 38, 38)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(downloadBtn)
                                            .addComponent(filechooseDropDown, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(uploadProgBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5))
                                    .addComponent(downloadProgBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(downloadPauseBtn)
                                        .addGap(37, 37, 37)))
                                .addGap(14, 14, 14)))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reset_btn))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(username_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(connect_btn)
                    .addComponent(disconnect_btn)
                    .addComponent(IP_addr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Chat_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(msg_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chat_choice_dropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(send_btn))))
                    .addComponent(users_txt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uploadProgBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(downloadBtn))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(downloadProgBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(downloadPauseBtn)
                                .addGap(43, 43, 43)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(reset_btn)
                                    .addComponent(jLabel1)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(filechooseDropDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(96, 96, 96))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchButton))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void send_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_send_btnActionPerformed
        try {
            // TODO add your handling code here:
            String msg_choice = chat_choice_dropdown.getSelectedItem();
            msg_txtActionPerformed(evt);
            Client.sendMessage(msg, msg_choice);

        } catch (IOException ex) {
            System.out.println("SENDING ERROR" + ex);
        }
    }//GEN-LAST:event_send_btnActionPerformed

    private void username_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_username_txtActionPerformed
        // TODO add your handling code here:
        username = username_txt.getText();

    }//GEN-LAST:event_username_txtActionPerformed

    //Calls the connect method to connect to the client
    //Checks the username entered
    private void connect_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connect_btnActionPerformed
        try {
            // TODO add your handling code here:
            boolean validIP = false;
            username_txtActionPerformed(evt);
            IP_addrActionPerformed(evt);
            if (username.equals("All")) {
                username = "All" + (int) (Math.random() * 100);
                JOptionPane.showMessageDialog(rootPane, "Cannot choose 'ALL' as your username. Your new Username is : " + username);
            }
            Client.connect(serverName, username);
            reset_btn.setEnabled(true);
            connected = true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Server not responding : " + ex);
        }
    }//GEN-LAST:event_connect_btnActionPerformed

    private void msg_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_msg_txtActionPerformed
        // TODO add your handling code here:
        msg = msg_txt.getText();

    }//GEN-LAST:event_msg_txtActionPerformed

    private void disconnect_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnect_btnActionPerformed
        // TODO add your handling code here:
        Client.disconnect(username);
        dispose();
    }//GEN-LAST:event_disconnect_btnActionPerformed

    private void IP_addrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IP_addrActionPerformed
        // TODO add your handling code here:
        IP = IP_addr.getText();

    }//GEN-LAST:event_IP_addrActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        dispose();
        Client.disconnect(username);

    }//GEN-LAST:event_formWindowClosing

    //Closes all DataStreams and closes socket
    //Resets the entire gui to be used for a new server
    //Only to be used when you want to disconnect or if the server disconnected
    private void reset_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reset_btnActionPerformed

        try {
            // TODO add your handling code here:
            username = "";
            list = null;
            chat_choice_dropdown.removeAll();
            users_txt.setText("");
            Chat_txt.setText("");
            IP_addr.setText("Enter IP");
            username_txt.setText("Enter Username");

            Client.out.close();
            Client.outToServer.close();
            Client.in.close();
            Client.inFromServer.close();
            Client.client.close();
        } catch (Exception ex) {
            System.err.println("Error in reset : " + ex);
        }

        reset_btn.setEnabled(false);

    }//GEN-LAST:event_reset_btnActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
        String search = searchText.getText();
        System.out.println("search in GUI   : " + search );
        try {
            Client.sendMessage(search, "~");
        } catch (IOException ex) {
            Logger.getLogger(ChatInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void downloadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadBtnActionPerformed
        try {
            // TODO add your handling code here:
            String fileSelected = filechooseDropDown.getSelectedItem();
            Client.sendMessage(fileSelected, "*");
            ReceiverThread receiverThread = new ReceiverThread(fileSelected);
            receiverThread.start();
            Client.fileName = fileSelected;
        } catch (IOException ex) {
            Logger.getLogger(ChatInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_downloadBtnActionPerformed

    private void downloadPauseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadPauseBtnActionPerformed
        // TODO add your handling code here:
        Client.isPaused = !Client.isPaused;
        if(Client.isPaused){
            downloadPauseBtn.setBackground(Color.gray);
        } else {
            downloadPauseBtn.setBackground(Color.white);
        }
        
    }//GEN-LAST:event_downloadPauseBtnActionPerformed

    //Prints the received message in the chat_txt
    public void printMsg(String msg, String FromWho) {
        Chat_txt.append(FromWho + ":" + msg + "\n");
    }

    //prints the dis/connection of a user in chat_txt
    public void printConnection(String toWho, boolean connectDis) {
        if (connectDis) {
            Chat_txt.append(toWho + " is connected \n");
        } else {
            Chat_txt.append(toWho + "  disconnected\n");
        }

    }

    // Simply adds the new user to the dropdown list and to the list of connected clients
    public void addUsr(String usr) {
        users_txt.append(usr + "\n");
        chat_choice_dropdown.add(usr);

    }

    //Removes a disconnected user from the list of users and the dropodown list
    public void removeUsers(String user, String list_of_users) {
        List<String> tempList = Arrays.asList(list_of_users.split(","));
        list = tempList;
        printConnection(user, false);
        chat_choice_dropdown.removeAll();
        users_txt.setText("");
        users_txt.append("All \n");
        chat_choice_dropdown.add("All \n");
        for (int i = 0; i < list.size(); i++) {
            addUsr(list.get(i));
        }
    }

    //This is called everytime a new user connects
    //It updates the user list and also the dropdown list
    //It updates the global list of users
    public void addAllusers(String list_of_users) {
        String newUsr = "";
        chat_choice_dropdown.removeAll();
        users_txt.setText("");
        users_txt.append("All \n");
        chat_choice_dropdown.add("All");
        List<String> tempList = Arrays.asList(list_of_users.split(","));
        if (list == null) {
            list = tempList;
            newUsr = username;
        } else {
            for (String s : tempList) {
                if (!list.contains(s)) {
                    newUsr = s;
                }
            }
            list = tempList;
        }
        printConnection(newUsr, true);
        for (int i = 0; i < list.size(); i++) {
            addUsr(list.get(i));
        }
    }

    //Checks if the IP entered is a valid IP address
    public static boolean checkIP(String ip) {
        boolean valid = false;
        Scanner sc = new Scanner(ip);
        sc.useDelimiter("\\.");
        String part1 = sc.next();
        String part2 = sc.next();
        String part3 = sc.next();
        String part4 = sc.next();
        sc.close();
        int part3_i = Integer.parseInt(part3);
        int part4_i = Integer.parseInt(part4);
        if (part1.equals("146")) {
            if (part2.equals("232")) {
                if (part3_i >= 10 && part3_i <= 99) {
                    if (part4_i >= 100 && part4_i <= 255) {
                        valid = true;
                    }
                }
            }
        }

        return valid;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatInterface().setVisible(true);
            }
        });
    }
    
    public static void progressTheDownloadBar(int prog){
        downloadProgBar.setValue(prog);
    }
    
    public static void progressTheUploadBar(int prog){
        uploadProgBar.setValue(prog);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.TextArea Chat_txt;
    private javax.swing.JTextField IP_addr;
    private java.awt.Choice chat_choice_dropdown;
    private javax.swing.JButton connect_btn;
    private javax.swing.JButton disconnect_btn;
    private javax.swing.JButton downloadBtn;
    public static javax.swing.JButton downloadPauseBtn;
    public static javax.swing.JProgressBar downloadProgBar;
    public static java.awt.Choice filechooseDropDown;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField msg_txt;
    private javax.swing.JButton reset_btn;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextArea searchText;
    private javax.swing.JButton send_btn;
    public static javax.swing.JProgressBar uploadProgBar;
    private javax.swing.JTextField username_txt;
    private java.awt.TextArea users_txt;
    // End of variables declaration//GEN-END:variables

}
