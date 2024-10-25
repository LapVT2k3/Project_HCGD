package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import controller.ClientControl;
import controller.PacketListener;
import model.Match;
import model.Matching;
import model.Packet;
import model.Question;
import model.User;


public class GameFrame extends javax.swing.JFrame implements PacketListener {

    private int timeLeft = 15;
    private Timer timer;
    private User user1, user2;
    private Matching matching;
    private ClientControl clientCtr;

    public GameFrame() {
        initComponents();
    }
    
    public GameFrame(ClientControl clientCtr, Matching matching, User user) {
        initComponents();
        this.clientCtr = clientCtr;
        this.clientCtr.addPacketListener(this);

        this.user1 = user;
        System.out.println(user1);

        if (user.getId() == matching.getUser1().getId()) {
            this.user2 = matching.getUser2();
        } else if (user.getId() == matching.getUser2().getId()) {
            this.user2 = matching.getUser1();
        }
            
        System.out.println(user2);
        
        this.matching = matching;

        setDefault();

        question.setVisible(false);

        ArrayList<Object> list = new ArrayList<>();

        list.add(user1);

        list.add(matching);

        clientCtr.sendData(new Packet("ready", list));

    }

    private void setDefault() {
        name1.setText(user1.getName());
        name2.setText(user2.getName());
        score1.setText("0");
        score2.setText("0");
        answer1.setText("0000");
        answer2.setText("0000");
        avartar1.setIcon(new ImageIcon(getClass().getResource(user1.getAvatarLink())));
        avartar2.setIcon(new ImageIcon(getClass().getResource(user2.getAvatarLink())));
        answer1.setEditable(false);
    }

    private void startCountdown() {
        timer = new Timer(1000, (ActionEvent e) -> {
            if (timeLeft > 0) {
                txtTime.setText(String.valueOf(timeLeft));
                timeLeft--;
            } else {
                txtTime.setText("0");
                timer.stop();
                answer1.setEditable(false);
                ArrayList<Object> list = new ArrayList<>();
                list.add(user1);
                list.add(matching);
                String s = answer1.getText();
                if (s.equals("")) {
                    s = s + "0";
                }
                list.add(s);
                clientCtr.sendData(new Packet("answer", list));
                timeLeft = 15;
            }
        });
        timer.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        answer2 = new javax.swing.JLabel();
        txtTime = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        avartar2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        avartar1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        answer1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        name2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        name1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        score2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        score1 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btSend = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        question = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GameFrame");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(1500, 840));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        answer2.setFont(new java.awt.Font("SVN-Batman Forever Alternate", 0, 77)); // NOI18N
        answer2.setForeground(new java.awt.Color(255, 255, 255));
        answer2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        answer2.setText("1234");
        getContentPane().add(answer2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1046, 673, 370, 140));

        txtTime.setFont(new java.awt.Font("SVN-Batman Forever Alternate", 0, 56)); // NOI18N
        txtTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTime.setText("15");
        getContentPane().add(txtTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(965, 73, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/khung.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(888, 10, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nền.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(892, 24, -1, -1));

        avartar2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        avartar2.setText("1");
        getContentPane().add(avartar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1133, 300, 230, 295));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/khung avatar.png"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 250, 570, -1));

        avartar1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        avartar1.setText("1");
        getContentPane().add(avartar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(143, 302, 230, 295));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/khung avatar.png"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-60, 250, 570, -1));
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 670, -1, -1));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/khung điểm.png"))); // NOI18N
        jLabel8.setToolTipText("");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 650, 380, 190));

        answer1.setBackground(new java.awt.Color(36, 76, 246));
        answer1.setFont(new java.awt.Font("SVN-Batman Forever Alternate", 0, 77)); // NOI18N
        answer1.setForeground(new java.awt.Color(255, 255, 255));
        answer1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        answer1.setText("0000");
        answer1.setBorder(null);
        answer1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        answer1.setSelectionColor(new java.awt.Color(36, 76, 247));
        answer1.setVerifyInputWhenFocusTarget(false);
        getContentPane().add(answer1, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 682, 360, 120));

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/khung điểm.png"))); // NOI18N
        jLabel9.setToolTipText("");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 650, 380, 190));

        name2.setFont(new java.awt.Font("SVN-Comic Sans MS", 0, 36)); // NOI18N
        name2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        name2.setText("andt123");
        getContentPane().add(name2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 240, 210, 30));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/name2.png"))); // NOI18N
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 120, -1, 160));

        name1.setFont(new java.awt.Font("SVN-Comic Sans MS", 0, 36)); // NOI18N
        name1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        name1.setText("andt123");
        getContentPane().add(name1, new org.netbeans.lib.awtextra.AbsoluteConstraints(203, 226, 210, 30));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/name1.png"))); // NOI18N
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, -1, 160));

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("SVN-Cookies", 0, 28)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Score:");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(1357, 145, -1, -1));

        score2.setFont(new java.awt.Font("SVN-Batman Forever Alternate", 1, 36)); // NOI18N
        score2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        score2.setText("55");
        getContentPane().add(score2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1380, 175, 70, 60));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/score.png"))); // NOI18N
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(1310, 100, 190, -1));

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("SVN-Cookies", 0, 28)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Score:");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(48, 145, -1, -1));

        score1.setFont(new java.awt.Font("SVN-Batman Forever Alternate", 1, 36)); // NOI18N
        score1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        score1.setText("55");
        getContentPane().add(score1, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 175, 80, 60));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/score.png"))); // NOI18N
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 190, -1));

        btSend.setFont(new java.awt.Font("SVN-Nexa Rush Slab Black Shadow", 1, 48)); // NOI18N
        btSend.setForeground(new java.awt.Color(255, 255, 255));
        btSend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btSend.setText("SEND");
        getContentPane().add(btSend, new org.netbeans.lib.awtextra.AbsoluteConstraints(479, 679, 260, 120));

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/button.png"))); // NOI18N
        jLabel23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel23MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 670, -1, -1));

        question.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        question.setText("Question");
        getContentPane().add(question, new org.netbeans.lib.awtextra.AbsoluteConstraints(553, 186, 400, 380));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/background.jpeg"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1500, 840));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseClicked
        timeLeft = 0;
    }//GEN-LAST:event_jLabel23MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new GameFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField answer1;
    private javax.swing.JLabel answer2;
    public javax.swing.JLabel avartar1;
    private javax.swing.JLabel avartar2;
    private javax.swing.JLabel btSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel name1;
    private javax.swing.JLabel name2;
    private javax.swing.JLabel question;
    private javax.swing.JLabel score1;
    private javax.swing.JLabel score2;
    private javax.swing.JLabel txtTime;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onPacketReceived(Packet packet) {
        if (null != packet.getHeader()) {
            switch (packet.getHeader()) {
                case "Question" -> {
                    Question q = (Question) packet.getContent();
                    System.out.println(q.toString());
                    System.out.println("Câu hỏi");
                    answer1.setText("");
                    answer2.setText("");
                    startCountdown();
                    answer1.setEditable(true);
                    break;
                }
                case "Answer" -> {
                    String answer = packet.getContent().toString();
                    System.out.println(answer);
                    answer2.setText(answer);
                    break;
                }
                case "Win" -> {
                    double i = Double.parseDouble(score1.getText());
                    score1.setText(String.valueOf(i + 1));
                    String s = packet.getContent().toString();
                    System.out.println(s);
                    break;
                }
                case "Lose" -> {
                    double i = Double.parseDouble(score2.getText());
                    score2.setText(String.valueOf(i + 1));
                    String s = packet.getContent().toString();
                    System.out.println(s);
                    break;
                }
                case "Draw" -> {
                    double i = Double.parseDouble(score1.getText());
                    score1.setText(String.valueOf(i + 0.5));
                    double j = Double.parseDouble(score2.getText());
                    score2.setText(String.valueOf(j + 0.5));
                    String s = packet.getContent().toString();
                    System.out.println(s);
                    break;
                }
                case "Endgame" -> {
                   this.dispose();
                   break;
                }
                default -> {

                }
            }
        }
    }
}
