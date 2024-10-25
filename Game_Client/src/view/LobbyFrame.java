package view;

import controller.ClientControl;
import controller.PacketListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.Match;
import model.Matching;
import model.Packet;
import model.User;

/**
 *
 * @author ADMIN
 */
public class LobbyFrame extends javax.swing.JFrame implements PacketListener {
    
    private User user;
    private ArrayList<User> listUser;
    private int statusTable;
    private ClientControl clientCtr;
    
    /**
     * Creates new form LobbyFrame
     */
    
    public LobbyFrame() {
        initComponents();
    }
    
    public LobbyFrame(User user, ClientControl clientCtr) {
        initComponents();
        this.clientCtr = clientCtr;
        this.clientCtr.addPacketListener(this);
        this.user = user;
        this.lbName.setText(user.getName());
        this.lbRank.setText("Rank: " + user.getScoreRank());
        this.lbAvatar.setIcon(new ImageIcon(getClass().getResource(user.getAvatarLink())));
        this.statusTable = 0;
        updateListUser();
        tblStatusRank.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    int row = tblStatusRank.rowAtPoint(e.getPoint());
                    if (statusTable == 0 && row >= listUser.indexOf(user)) {
                        row += 1;
                    }
                    User selectedUser = listUser.get(row);
                    if (row != -1) {
                        showPlayerOptionsDialog(LobbyFrame.this, selectedUser);
                    }
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        lbAvatar = new javax.swing.JLabel();
        lbName = new javax.swing.JLabel();
        lbRank = new javax.swing.JLabel();
        btnSetting = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStatusRank = new javax.swing.JTable();
        btnStatusTable = new javax.swing.JButton();
        btnLeaderBoard = new javax.swing.JButton();

        jLabel4.setText("jLabel4");

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbAvatar.setText("Avatar");

        lbName.setText("Name");

        lbRank.setText("Rank");

        btnSetting.setText("Setting");
        btnSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSettingActionPerformed(evt);
            }
        });

        tblStatusRank.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Danh sách người chơi"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblStatusRank);
        if (tblStatusRank.getColumnModel().getColumnCount() > 0) {
            tblStatusRank.getColumnModel().getColumn(0).setResizable(false);
        }

        btnStatusTable.setText("Danh sách người chơi");
        btnStatusTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatusTableActionPerformed(evt);
            }
        });

        btnLeaderBoard.setText("Bảng xếp hạng");
        btnLeaderBoard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaderBoardActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lbAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 131, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnStatusTable)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnLeaderBoard, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbName, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSetting)))
                        .addGap(17, 17, 17))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbRank, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbAvatar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSetting))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbRank)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStatusTable)
                    .addComponent(btnLeaderBoard))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettingActionPerformed
        // TODO add your handling code here:
        SettingFrame settingFrame = new SettingFrame(user, clientCtr);
        settingFrame.setVisible(true);
    }//GEN-LAST:event_btnSettingActionPerformed

    private void btnStatusTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatusTableActionPerformed
        // TODO add your handling code here:
        this.statusTable = 0;
        updateListUser();
    }//GEN-LAST:event_btnStatusTableActionPerformed

    private void btnLeaderBoardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaderBoardActionPerformed
        // TODO add your handling code here:
        this.statusTable = 1;
        updateListUser();
    }//GEN-LAST:event_btnLeaderBoardActionPerformed
    
    public void showPlayerOptionsDialog(JFrame parent, User selectedUser) {
        Object[] options = {"Mời chơi", "Xem lịch sử đấu", "Hủy"};
        int choice = JOptionPane.showOptionDialog(parent,
                "Chọn hành động cho người chơi: " + selectedUser.getName(),
                "Tùy chọn người chơi",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[2]);

        switch (choice) {
            case JOptionPane.YES_OPTION:
                sendInvitePlayGame(selectedUser);
                break;
            case JOptionPane.NO_OPTION:
                viewHistory(selectedUser);
                break;
            case JOptionPane.CANCEL_OPTION:
                break;
            default:
                break;
        }
    }
    
    public void showAcceptDenyOptionsDialog(JFrame parent, ArrayList<User> invitation) {
        Object[] options = {"Đồng ý", "Từ chối"};
        int choice = JOptionPane.showOptionDialog(parent,
                "Người chơi " + invitation.get(0).getName() + " gửi lời thách đấu.",
                "Tùy chọn người chơi",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case JOptionPane.YES_OPTION:
                sendResponseInvitation(invitation, 1);
                break;
            case JOptionPane.NO_OPTION:
                sendResponseInvitation(invitation, 0);
                break;
            default:
                break;
        }
    }
    
    public void sendResponseInvitation(ArrayList<User> invitation, int option) {
        ArrayList<Object> responseInvitation = new ArrayList<>();
        responseInvitation.add(invitation);
        if (option == 1) {
            responseInvitation.add("accept");
            clientCtr.sendData(new Packet("response_invitation", responseInvitation));
        } else {
            responseInvitation.add("deny");
            clientCtr.sendData(new Packet("response_invitation", responseInvitation));
        }
    }
    
    public void updateTable(int statusTable) {
        DefaultTableModel model = (DefaultTableModel) tblStatusRank.getModel();
        model.setRowCount(0);
        if (statusTable == 0) {
            for (User user : this.listUser) {
                if (user.equals(this.user)) continue;
                model.addRow(new Object[]{
                    String.format("%-20s %-12s %-10s", 
                        user.getName(), 
                        "Score: " + user.getScoreRank(), 
                        "Status: " + user.getStatus())
                });
            }
        } else if (statusTable == 1) {
            Collections.sort(this.listUser, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return Integer.compare(o2.getScoreRank(), o1.getScoreRank());
                }
            });
            for (User user : this.listUser) {
                model.addRow(new Object[]{
                    String.format("%-20s %-12s %-10s", 
                        user.getName() + " " + (user.equals(this.user) ? "(You)" : ""), 
                        "Score: " + user.getScoreRank(), 
                        "Status: " + user.getStatus())
                });
            }
        }
    }
    
    public void sendInvitePlayGame(User invitee) {
        ArrayList<User> invitation = new ArrayList<>();
        invitation.add(this.user);
        invitation.add(invitee);
        clientCtr.sendData(new Packet("send_invitation", invitation));
    }
    
    
    public void viewHistory(User user) {
        HistoryMatchFrame frameHistoryMatch = new HistoryMatchFrame(user, clientCtr);
        frameHistoryMatch.setVisible(true);
        
    }
    
    public void updateListUser() {
        clientCtr.sendData(new Packet("list", null));
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
            java.util.logging.Logger.getLogger(LobbyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LobbyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LobbyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LobbyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LobbyFrame().setVisible(true);
            }
        });
    }
    
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLeaderBoard;
    private javax.swing.JButton btnSetting;
    private javax.swing.JButton btnStatusTable;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lbAvatar;
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbRank;
    private javax.swing.JTable tblStatusRank;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onPacketReceived(Packet packet) {
        if (null != packet.getHeader()) switch (packet.getHeader()) {
            case "another_login":
                int userLoginId = (int) packet.getContent();
                for (User user1: listUser) {
                    if (user1.getId() == userLoginId) {
                        user1.setStatus(1);
                        break;
                    }
                }
                updateTable(statusTable);
                break;
            case "another_logout":
                int userLogoutId = (int) packet.getContent();
                for (User user: listUser) {
                    if (user.getId() == userLogoutId) {
                        user.setStatus(0);
                        break;
                    }
                }
                updateTable(statusTable);
                break;
            case "Endgame":
                ArrayList<User> user_update = (ArrayList<User>) packet.getContent();
                User user_1 = user_update.get(0);
                User user_2 = user_update.get(1);
                if (this.user.getId() == user_1.getId()) {
                    this.user = user_1;
                } else if (this.user.getId() == user_2.getId()) {
                    this.user = user_2;
                }
                for (User user: listUser) {
                    if (user.getId() == user_1.getId()) {
                        user.setStatus(1);
                        user.setScoreRank(user_1.getScoreRank());
                    } else if (user.getId() == user_2.getId()) {
                        user.setStatus(1);
                        user.setScoreRank(user_2.getScoreRank());
                    }
                }
                updateTable(statusTable);
                this.setVisible(true);
                break;
            case "another_start":
                ArrayList<Integer> user_start = (ArrayList<Integer>) packet.getContent();
                int user1_id_s = user_start.get(0);
                int user2_id_s = user_start.get(1);
                for (User user1: listUser) {
                    if (user1.getId() == user1_id_s || user1.getId() == user2_id_s) {
                        user1.setStatus(2);
                    }
                }
                updateTable(statusTable);
                break;
            case "another_end":
                ArrayList<User> user_end = (ArrayList<User>) packet.getContent();
                User user1 = user_end.get(0);
                User user2 = user_end.get(1);
                for (User user: listUser) {
                    if (user.getId() == user1.getId()) {
                        user.setStatus(1);
                        user.setScoreRank(user1.getScoreRank());
                    } else if (user.getId() == user2.getId()) {
                        user.setStatus(1);
                        user.setScoreRank(user2.getScoreRank());
                    }
                }
                updateTable(statusTable);
                break;
            case "list_ok":
                ArrayList<User> listUser = (ArrayList<User>) packet.getContent();
                this.listUser = listUser;
                updateTable(statusTable);
                break;
            case "list_fail":
                showMessage("Lấy danh sách thất bại!");
                break;
            case "invite_sent":
                showMessage("Đã gửi lời mời thành công!");
                break;
            case "offline":
                showMessage("Người chơi đang offline!");
                break;
            case "recieve_invitation":
                ArrayList<User> invitation = (ArrayList<User>) packet.getContent();
                showAcceptDenyOptionsDialog(this, invitation);
                break;
            case "start_game":
                Matching matching = (Matching) packet.getContent();
                // Cập nhật lại status
                if (this.user.getId() == matching.getUser1().getId()) {
                    this.user.setStatus(matching.getUser1().getStatus());
                } else if (this.user.getId() == matching.getUser2().getId()) {
                    this.user.setStatus(matching.getUser2().getStatus());
                }
                
                GameFrame gameFrame = new GameFrame(clientCtr, matching, user);
                gameFrame.setVisible(true);
                this.setVisible(false);
                break;
            case "deny_invitation":
                invitation = (ArrayList<User>) packet.getContent();
                showMessage("Người chơi " + invitation.get(1).getName() + " từ chối!");
                break;
            case "recieve_history":
                ArrayList<Match> listMatch = (ArrayList<Match>) packet.getContent();
                HistoryMatchFrame frameHistoryMatch = new HistoryMatchFrame();
                break;
            case "inviter_busy":
                showMessage("Người mời đã bận!");
                break;
            case "update_ok":
                User user = (User) packet.getContent();
                this.user = user;
                this.lbName.setText(user.getName());
                this.lbRank.setText("Rank: " + user.getScoreRank());
                break;
            case "logout_ok":
                this.setVisible(false);
                break; 
            default:
                break;
        }
    }
}
