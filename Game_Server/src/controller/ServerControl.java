package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Matching;
import model.Match;
import model.Packet;
import model.Question;
import model.User;

/**
 *
 * @author ADMIN
 */
public class ServerControl {
    private ServerSocket serverSocket;
    private Connection con;
    private int serverPort = 8888;
    private HashMap<Integer, ObjectOutputStream> onlineUsers = new HashMap<>();

    public ServerControl() {
        getDBConnection("dblogin", "root", "123456");
        openServer(serverPort);
        System.out.println("Server is running!");
        while (true) {
            listenning();
        }
    }
    
    private void getDBConnection(String dbName, 
            String username, String password) {
        String dbUrl = "jdbc:mysql://localhost:3306/" + dbName;
        String dbClass = "com.mysql.cj.jdbc.Driver";
        
        try {
            Class.forName(dbClass);
            con = (Connection) DriverManager.getConnection(dbUrl, username, password);
            System.out.println("Connect successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void openServer(int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void listenning() {
        try {
            Socket clientSocket = serverSocket.accept();
            String clientIp = clientSocket.getInetAddress().getHostAddress();
            System.out.println("Client connected: " + clientIp);
            new Thread(() -> {
                try {
                    handleClient(clientSocket);
                } catch (Exception ex) {
                    Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void handleClient(Socket clientSocket) throws Exception {
        User user = null;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            while (true) {
                Packet packet = (Packet) ois.readObject();
                if (packet != null && packet.getHeader() != null) {
                    int logoutStatus = 0;
                    switch (packet.getHeader()) {
                        case "login":
                            user = handleLogin(packet, oos);
                            break;
                        case "signUp":
                            handleSignUp(packet, oos);
                            break;
                        case "list":
                            handleListUsers(oos);
                            break;
                        case "send_invitation":
                            handleSendInvitation(packet, oos);
                            break;
                        case "response_invitation":
                            handleResponseInvitation(packet, oos);
                            break;
                        case "history":
                            handleListMatches(packet, oos);
                            break;
                        case "update_user":
                            user = handleUpdate(packet, oos);
                            break;
                        case "ready":
                            startGame(packet, oos);
                            break;
                        case "answer":
                            checkAnswer(packet, oos);
                            break;    
                        case "logout":
                            handleLogout(packet, oos);
                            logoutStatus = 1;
                            break;
                        default:
                            System.out.println("Unknown header: " + packet.getHeader());
                    }
                    if (logoutStatus == 1) break;
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            if (user != null) {
                System.out.println("Nguời chơi ID = " + user.getId() + " vừa ngắt kết nối đột ngột!");
                user = getUserById(user.getId());
                user.setStatus(0);
                updateUser(user);
                onlineUsers.remove(user.getId());
                for (ObjectOutputStream oos1: onlineUsers.values()) {
                    oos1.writeObject(new Packet("another_logout", user.getId()));
                }
            }
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private User handleLogin(Packet packet, ObjectOutputStream oos) throws Exception {
        User user = (User) packet.getContent();
        User completeUser = checkUser(user);
        if (completeUser != null) {
            completeUser.setStatus(1);
            updateUser(completeUser);
            oos.writeObject(new Packet("login_ok", completeUser));
            
            for (ObjectOutputStream oos1: onlineUsers.values()) {
                oos1.writeObject(new Packet("another_login", completeUser.getId()));
            }
            onlineUsers.put(completeUser.getId(), oos);
            
        } else {
            oos.writeObject(new Packet("login_false", null));
        }
        return completeUser;
    }
    
    private void handleSignUp(Packet packet, ObjectOutputStream oos) throws Exception {
        User user = (User) packet.getContent();
        if (!checkUserExists(user.getUserName())) {
            if (createUser(user)) {
                System.out.println("Đăng ký thành công!");
                oos.writeObject(new Packet("signUp_ok", ""));
            } else {
                oos.writeObject(new Packet("signUp_false", ""));
            }
        } else {
            oos.writeObject(new Packet("signUp_false", ""));
        }
    }
    
    private void handleLogout(Packet packet, ObjectOutputStream oos) throws Exception {
        User user = (User) packet.getContent();
        user.setStatus(0);
        updateUser(user);

        onlineUsers.remove(user.getId());
        for (ObjectOutputStream oos1: onlineUsers.values()) {
            oos1.writeObject(new Packet("another_logout", user.getId()));
        }
        oos.writeObject(new Packet("logout_ok", null));
    }
    
    private User handleUpdate(Packet packet, ObjectOutputStream oos) throws Exception {
        User user = (User)packet.getContent();
        updateUser(user);
        oos.writeObject(new Packet("update_ok", user));
        return user;
    }
    
    private void handleListUsers(ObjectOutputStream oos) {
        try {
            ArrayList<User> listUser = getListUser();
            if (listUser != null) {
                oos.writeObject(new Packet("list_ok", listUser));
            } else {
                oos.writeObject(new Packet("list_false", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleListMatches(Packet packet, ObjectOutputStream oos) {
        try {
            User user = (User)packet.getContent();
            ArrayList<Match> listMatch = getListMatch(user);
            if (listMatch != null) {
                oos.writeObject(new Packet("history_ok", listMatch));
            } else {
                oos.writeObject(new Packet("history_false", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
    
    private void handleSendInvitation(Packet packet, ObjectOutputStream oos) throws IOException {
        ArrayList<User> invitation = (ArrayList<User>) packet.getContent();
        ObjectOutputStream oos_invitee = onlineUsers.get(invitation.get(1).getId());
        if (oos_invitee != null) {
            oos_invitee.writeObject(new Packet("recieve_invitation", invitation));
//            oos.writeObject(new Packet("invite_sent", null));
        } else {
            oos.writeObject(new Packet("offline", null));
        }
    }
    
    // Hàm cập nhật trạng thái người chơi trong trận đấu
    private void updateMatchingStatus(int userid, int matchid) throws SQLException {
        String query = "UPDATE matching SET "
                + "status1 = CASE WHEN user1_id = " + userid + " THEN 1 ELSE status1 END, "
                + "status2 = CASE WHEN user2_id = " + userid + " THEN 1 ELSE status2 END "
                + "WHERE (user1_id = " + userid + " OR user2_id = " + userid + ") AND id = " + matchid;
        Statement pstmt = con.prepareStatement(query);
        pstmt.executeUpdate(query);
    }
    
    // Hàm lấy thông tin trận đấu từ bảng matching
    private ResultSet getMatchInfo(int matchid) throws SQLException {
        String sqlCheck = "SELECT * FROM matching WHERE id = " + matchid;
        Statement pstmt2 = con.prepareStatement(sqlCheck);
        return pstmt2.executeQuery(sqlCheck);
    }
    
    public ObjectOutputStream findUser(int userId) {
        if (onlineUsers.containsKey(userId)) {
            return onlineUsers.get(userId);
        } else {
            return null;
        }
    }
    
    // Hàm lấy một câu hỏi ngẫu nhiên từ bảng questions
    private Question getRandomQuestion() throws SQLException {
        String query = "SELECT * FROM questions WHERE type = 1 ORDER BY RAND() LIMIT 1";
        Statement pstmt3 = con.prepareStatement(query);
        ResultSet rs3 = pstmt3.executeQuery(query);
        if (rs3.next()) {
            return new Question(rs3.getInt("id"), rs3.getString("linkImg"), rs3.getInt("type"), rs3.getInt("answer"));
        } else {
            System.out.println("Không tìm thấy câu hỏi nào với type = 1.");
            return null;
        }
    }
    
    // Hàm gửi câu hỏi đến hai người chơi
    private void sendQuestionToUsers(int user1_id, int user2_id, Question q, int answer, int matchid) throws SQLException, IOException {
        if (findUser(user1_id) != null && findUser(user2_id) != null) {
            String query = "UPDATE matching SET "
                    + "status1 = 0, "
                    + "status2 = 0, "
                    + "answerQuestionNow = " + answer + ", "
                    + "statusMatch = statusMatch + 1 "
                    + "WHERE id = " + matchid;
            Statement pstmt4 = con.prepareStatement(query);
            pstmt4.executeUpdate(query);

            findUser(user1_id).writeObject(new Packet("Question", q));
            findUser(user2_id).writeObject(new Packet("Question", q));
            System.out.println("Đã gửi câu hỏi cho người chơi.");
        }
    }
    
    private synchronized void startGame(Packet packet, ObjectOutputStream oos) {
        Object content = packet.getContent();
        if (content instanceof ArrayList) {
            ArrayList<Object> list = (ArrayList<Object>) content;
            int userid = 0;
            int matchid = 0;
            if (list.get(0) instanceof User) {
                userid = ((User) list.get(0)).getId();
            }
            if (list.get(1) instanceof Matching) {
                matchid = ((Matching) list.get(1)).getId();
            }
            try {
                // Cập nhật trạng thái của người chơi
                updateMatchingStatus(userid, matchid);

                // Lấy thông tin trận đấu
                ResultSet rs2 = getMatchInfo(matchid);
                if (rs2.next()) {
                    int user1_id = rs2.getInt("user1_id");
                    int user2_id = rs2.getInt("user2_id");
                    int status1 = rs2.getInt("status1");
                    int status2 = rs2.getInt("status2");

                    if (status1 == 1 && status2 == 1) {
                        System.out.println("Cả status1 và status2 đều là 1.");
                        ArrayList<Integer> user_start = new ArrayList<>();
                        user_start.add(user1_id);
                        user_start.add(user2_id);
                        for (int i: onlineUsers.keySet()) {
                            if (i != user1_id && i != user2_id) 
                                onlineUsers.get(i).writeObject(new Packet("another_start", user_start));
                        }
                        // Lấy câu hỏi ngẫu nhiên
                        Question q = getRandomQuestion();
                        if (q != null) {
                            // Gửi câu hỏi cho cả hai người chơi
                            sendQuestionToUsers(user1_id, user2_id, q, q.getAnswer(), matchid);
                        }
                    } else {
                        System.out.println("Một trong hai status không phải là 1.");
                    }
                } else {
                    System.out.println("Không tìm thấy trận đấu với ID đã cho.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // Hàm cập nhật trạng thái và câu trả lời của người chơi
    private void updateAnswerStatus(int userid, int matchid, String answer) throws SQLException {
        String query = "UPDATE matching SET "
                + "status1 = CASE WHEN user1_id = " + userid + " THEN 1 ELSE status1 END, "
                + "status2 = CASE WHEN user2_id = " + userid + " THEN 1 ELSE status2 END, "
                + "answer1 = CASE WHEN user1_id = " + userid + " THEN " + answer + " ELSE answer1 END, "
                + "answer2 = CASE WHEN user2_id = " + userid + " THEN " + answer + " ELSE answer2 END "
                + "WHERE (user1_id = " + userid + " OR user2_id = " + userid + ") AND id = " + matchid;
        Statement pstmt = con.prepareStatement(query);
        pstmt.executeUpdate(query);
    }
    
    // Hàm so sánh câu trả lời của hai người chơi
    private void compareAnswers(int user1_id, int user2_id, int answer1, int answer2, int answerQuestionNow, int matchid) throws SQLException, IOException {
        if (Math.abs(answerQuestionNow - answer1) > Math.abs(answerQuestionNow - answer2)) {
            // Người chơi 2 thắng
            findUser(user1_id).writeObject(new Packet("Lose", "Rất tiếc, đối thủ đã dự đoán chính xác hơn!"));
            findUser(user2_id).writeObject(new Packet("Win", "Tuyệt vời, bạn đã dự đoán chính xác hơn và nhận được 1 điểm thưởng!"));

            // Cập nhật điểm của người chơi 2
            String query = "UPDATE matching SET scoreUser2 = scoreUser2 + 1 WHERE id = " + matchid;
            Statement pstmt = con.prepareStatement(query);
            pstmt.executeUpdate(query);

        } else if (Math.abs(answerQuestionNow - answer1) < Math.abs(answerQuestionNow - answer2)) {
            // Người chơi 1 thắng
            findUser(user2_id).writeObject(new Packet("Lose", "Rất tiếc, đối thủ đã dự đoán chính xác hơn!"));
            findUser(user1_id).writeObject(new Packet("Win", "Tuyệt vời, bạn đã dự đoán chính xác hơn và nhận được 1 điểm thưởng!"));

            // Cập nhật điểm của người chơi 1
            String query = "UPDATE matching SET scoreUser1 = scoreUser1 + 1 WHERE id = " + matchid;
            Statement pstmt = con.prepareStatement(query);
            pstmt.executeUpdate(query);

        } else {
            // Hai người chơi hòa
            findUser(user1_id).writeObject(new Packet("Draw", "Thật cân tài cân sức, cả 2 đem về nửa điểm thưởng!"));
            findUser(user2_id).writeObject(new Packet("Draw", "Thật cân tài cân sức, cả 2 đem về nửa điểm thưởng!"));

            // Cập nhật điểm cho cả hai người chơi
            String query = "UPDATE matching SET scoreUser1 = scoreUser1 + 0.5, scoreUser2 = scoreUser2 + 0.5 WHERE id = " + matchid;
            Statement pstmt = con.prepareStatement(query);
            pstmt.executeUpdate(query);
        }
    }
    
    // Hàm gửi kết quả đến hai người chơi
    private void sendResultToUsers(int user1_id, int user2_id, String answer, int userid) throws IOException {
        if (user1_id == userid) {
            findUser(user2_id).writeObject(new Packet("Answer", answer));
        }
        if (user2_id == userid) {
            findUser(user1_id).writeObject(new Packet("Answer", answer));
        }
    }
    
    // Hàm chuẩn bị và gửi câu hỏi tiếp theo nếu trận đấu chưa kết thúc
    private void prepareNextQuestion(int matchid, int user1_id, int user2_id, int statusMatch) throws SQLException, IOException {
        if (statusMatch == 5) {
            updateMatchScores(matchid);
            updateUserScoreAndState(matchid);

            User user1 = getUserById(user1_id);
            User user2 = getUserById(user2_id);
            ArrayList<User> user_update = new ArrayList<>();
            user_update.add(user1);
            user_update.add(user2);

            findUser(user1_id).writeObject(new Packet("Endgame", user_update));
            findUser(user2_id).writeObject(new Packet("Endgame", user_update));
            
            ArrayList<User> user_end = new ArrayList<>();
            user_end.add(user1);
            user_end.add(user2);
            for (int i: onlineUsers.keySet()) {
                if (i != user1.getId() && i != user2.getId()) 
                    onlineUsers.get(i).writeObject(new Packet("another_end", user_end));
            }
        } else {
            Question q = null;
            statusMatch++;
            String query = "SELECT * FROM questions WHERE type = " + statusMatch + " ORDER BY RAND() LIMIT 1";
            Statement pstmt3 = con.prepareStatement(query);
            ResultSet rs2 = pstmt3.executeQuery(query);
            if (rs2.next()) {
                q = new Question(rs2.getInt("id"), rs2.getString("linkImg"), rs2.getInt("type"), rs2.getInt("answer"));
                System.out.println(q.toString());

                String updateQuery = "UPDATE matching SET "
                        + "status1 = 0, "
                        + "status2 = 0, "
                        + "answer1 = 0, "
                        + "answer2 = 0, "
                        + "answerQuestionNow = " + rs2.getInt("answer") + ", "
                        + "statusMatch = statusMatch + 1 "
                        + "WHERE id = " + matchid;
                Statement pstmt4 = con.prepareStatement(updateQuery);
                pstmt4.executeUpdate(updateQuery);

                findUser(user1_id).writeObject(new Packet("Question", q));
                findUser(user2_id).writeObject(new Packet("Question", q));
            } else {
                System.out.println("Không tìm thấy câu hỏi nào với type = " + statusMatch);
            }
        }
    }
    
    private synchronized void checkAnswer(Packet packet, ObjectOutputStream oos) {
        Object content = packet.getContent();
        if (content instanceof ArrayList) {
            ArrayList<Object> list = (ArrayList<Object>) content;
            int userid = 0;
            int matchid = 0;
            String answer = null;
            if (list.get(0) instanceof User) {
                userid = ((User) list.get(0)).getId();
            }
            if (list.get(1) instanceof Matching) {
                matchid = ((Matching) list.get(1)).getId();
            }
            if (list.get(2) instanceof String) {
                answer = list.get(2).toString();
            }
            try {
                // Cập nhật trạng thái và câu trả lời của người chơi
                updateAnswerStatus(userid, matchid, answer);

                // Lấy thông tin trận đấu
                ResultSet rs = getMatchInfo(matchid);
                if (rs.next()) {
                    int user1_id = rs.getInt("user1_id");
                    int user2_id = rs.getInt("user2_id");
                    int status1 = rs.getInt("status1");
                    int status2 = rs.getInt("status2");
                    int answer1 = rs.getInt("answer1");
                    int answer2 = rs.getInt("answer2");
                    int answerQuestionNow = rs.getInt("answerQuestionNow");
                    int statusMatch = rs.getInt("statusMatch");

                    // Gửi câu trả lời đến đối thủ
                    sendResultToUsers(user1_id, user2_id, answer, userid);

                    // So sánh câu trả lời nếu cả hai đều đã trả lời
                    if (status1 == 1 && status2 == 1) {
                        compareAnswers(user1_id, user2_id, answer1, answer2, answerQuestionNow, matchid);

                        // Chuẩn bị câu hỏi tiếp theo
                        prepareNextQuestion(matchid, user1_id, user2_id, statusMatch);
                    } else {
                        System.out.println("Một trong hai người chơi chưa trả lời.");
                    }
                } else {
                    System.out.println("Không tìm thấy trận đấu với ID đã cho.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void handleResponseInvitation(Packet packet, ObjectOutputStream oos) throws Exception {
        ArrayList<Object> responseInvitation = (ArrayList<Object>) packet.getContent();
        ArrayList<User> invitation = (ArrayList<User>) responseInvitation.get(0);
        String response = (String) responseInvitation.get(1);
        ObjectOutputStream oos_inviter = onlineUsers.get(invitation.get(0).getId());
        if (oos_inviter != null) {
            if (response.equals("accept")) {
                User user1 = invitation.get(0);
                User user2 = invitation.get(1);
                user1.setStatus(2);
                user2.setStatus(2);
                updateUser(user1);
                updateUser(user2);
                Matching matching = createAndGetMatching(user1, user2);
                oos.writeObject(new Packet("start_game", matching));
                oos_inviter.writeObject(new Packet("start_game", matching));
            } else {
                oos_inviter.writeObject(new Packet("deny_invitation", invitation));
            }
        } else {
            oos.writeObject(new Packet("inviter_busy", null));
        }
    }
    
    private int createMatch(User user1, User user2) throws Exception {
        int scoreUser1 = 0;
        int scoreUser2 = 0;
        LocalDateTime timeStart = LocalDateTime.now();

        String query = "INSERT INTO matches (user1_id, user2_id, startTime, scoreUser1, scoreUser2) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, user1.getId());
            ps.setInt(2, user2.getId());
            ps.setTimestamp(3, Timestamp.valueOf(timeStart));
            ps.setInt(4, scoreUser1);
            ps.setInt(5, scoreUser2);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return -1;
    }

    private boolean createMatching(User user1, User user2, int matchId) throws Exception {
        int status1 = 0;
        int status2 = 0;
        int answer1 = 0;
        int answer2 = 0;
        int statusMatch = 0;
        int scoreUser1 = 0;
        int scoreUser2 = 0;
        int answerQuestionNow = 0;

        String query = "INSERT INTO matching (user1_id, user2_id, status1, status2, answer1, answer2, statusMatch, scoreUser1, scoreUser2, answerQuestionNow, id)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user1.getId());
            ps.setInt(2, user2.getId());
            ps.setInt(3, status1);
            ps.setInt(4, status2);
            ps.setInt(5, answer1);
            ps.setInt(6, answer2);
            ps.setInt(7, statusMatch);
            ps.setInt(8, scoreUser1);
            ps.setInt(9, scoreUser2);
            ps.setInt(10, answerQuestionNow);
            ps.setInt(11, matchId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw e;
        }
    }

    private Matching getMatching(User user1, User user2) throws Exception {
        String getMatchingQuery = "SELECT * FROM matching WHERE user1_id = ? AND user2_id = ? ORDER BY id DESC LIMIT 1";

        try {
            PreparedStatement getMatchingPs = con.prepareStatement(getMatchingQuery);
            getMatchingPs.setInt(1, user1.getId());
            getMatchingPs.setInt(2, user2.getId());

            ResultSet rs = getMatchingPs.executeQuery();
            if (rs.next()) {
                return new Matching(
                    rs.getInt("id"),
                    getUserById(rs.getInt("user1_id")),
                    getUserById(rs.getInt("user2_id")),
                    rs.getInt("status1"),
                    rs.getInt("status2"),
                    rs.getInt("answer1"),
                    rs.getInt("answer2"),
                    rs.getInt("statusMatch"),
                    rs.getFloat("scoreUser1"),
                    rs.getFloat("scoreUser2"),
                    rs.getInt("answerQuestionNow")
                );
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    private Matching createAndGetMatching(User user1, User user2) throws Exception {
        int matchId = createMatch(user1, user2);
        if (matchId == -1) {
            throw new Exception("Error creating match record.");
        }

        if (!createMatching(user1, user2, matchId)) {
            throw new Exception("Error creating matching record.");
        }

        return getMatching(user1, user2);
    }
    
    private void updateUserScoreAndState(int matchid) throws SQLException {
        String selectQuery = "SELECT user1_id, user2_id, scoreUser1, scoreUser2 FROM matching WHERE id = ?";
        try {
            PreparedStatement selectStmt = con.prepareStatement(selectQuery);
            selectStmt.setInt(1, matchid);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int user1_id = rs.getInt("user1_id");
                int user2_id = rs.getInt("user2_id");
                float scoreUser1 = rs.getFloat("scoreUser1");
                float scoreUser2 = rs.getFloat("scoreUser2");
                int win = 5, lose = -3, draw = 1;

                String updateScoreQuery = "UPDATE users SET scoreRank = scoreRank + ? WHERE id = ?";
                PreparedStatement updateStmt = con.prepareStatement(updateScoreQuery);

                if (scoreUser1 == scoreUser2) {
                    updateStmt.setInt(1, draw);
                    updateStmt.setInt(2, user1_id);
                    updateStmt.executeUpdate();

                    updateStmt.setInt(1, draw);
                    updateStmt.setInt(2, user2_id);
                    updateStmt.executeUpdate();
                } else if (scoreUser1 > scoreUser2) {
                    updateStmt.setInt(1, win);
                    updateStmt.setInt(2, user1_id);
                    updateStmt.executeUpdate();

                    updateStmt.setInt(1, lose);
                    updateStmt.setInt(2, user2_id);
                    updateStmt.executeUpdate();
                } else {
                    updateStmt.setInt(1, lose);
                    updateStmt.setInt(2, user1_id);
                    updateStmt.executeUpdate();

                    updateStmt.setInt(1, win);
                    updateStmt.setInt(2, user2_id);
                    updateStmt.executeUpdate();
                }

                String updateStatusQuery = "UPDATE users SET status = 1 WHERE id = ? OR id = ?";
                PreparedStatement updateStatusStmt = con.prepareStatement(updateStatusQuery);
                updateStatusStmt.setInt(1, user1_id);
                updateStatusStmt.setInt(2, user2_id);
                updateStatusStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw e;
        }
}
    
    private void updateMatchScores(int matchid) throws SQLException {
        String selectQuery = "SELECT scoreUser1, scoreUser2 FROM matching WHERE id = ?";
        String updateQuery = "UPDATE matches SET scoreUser1 = ?, scoreUser2 = ? WHERE id = ?";

        try {
            PreparedStatement selectStmt = con.prepareStatement(selectQuery);
            selectStmt.setInt(1, matchid);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                float scoreUser1 = rs.getInt("scoreUser1");
                float scoreUser2 = rs.getInt("scoreUser2");

                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setFloat(1, scoreUser1);
                updateStmt.setFloat(2, scoreUser2);
                updateStmt.setInt(3, matchid);

                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    
    private User checkUser(User user) throws Exception {
        String query = "Select * FROM users WHERE username ='"
                + user.getUserName()
                + "' AND password ='" + user.getPassword() + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                User completeUser = new User();
                completeUser.setId(rs.getInt("id"));
                completeUser.setUserName(rs.getString("username"));
                completeUser.setPassword(rs.getString("password"));
                completeUser.setName(rs.getString("name"));
                completeUser.setAvatarLink(rs.getString("avatarLink"));
                completeUser.setScoreRank(rs.getInt("scoreRank"));
                completeUser.setStatus(rs.getInt("status"));
                return completeUser;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }
    
    private boolean checkUserExists(String username) throws SQLException{
        String query = "Select * FROM users WHERE username = '"
                + username + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs.next();
        } catch (SQLException e) {
            throw e;
        }
    }
    
    private boolean createUser(User user) throws SQLException {
        int scoreRank = 0;
        int status = 0;

        String query = "INSERT INTO users (username, password, name, avatarLink, scoreRank, status) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getAvatarLink());
            ps.setInt(5, scoreRank);
            ps.setInt(6, status);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw e;
        }
    }
    
    private void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET username = ?, password = ?, name = ?, avatarLink = ?, scoreRank = ?, status = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, user.getUserName());   
            preparedStatement.setString(2, user.getPassword());  
            preparedStatement.setString(3, user.getName());      
            preparedStatement.setString(4, user.getAvatarLink()); 
            preparedStatement.setInt(5, user.getScoreRank());   
            preparedStatement.setInt(6, user.getStatus());       
            preparedStatement.setInt(7, user.getId());        

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
    
    private ArrayList<User> getListUser() throws SQLException {
        ArrayList<User> userList = new ArrayList<>();
        String query = "Select * FROM users";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            User user = null;
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setAvatarLink(rs.getString("avatarLink"));
                user.setScoreRank(rs.getInt("scoreRank"));
                user.setStatus(rs.getInt("status"));
                userList.add(user);
            }
        } catch (SQLException e) {
            throw e;
        }
        return userList;
    }
    
    private ArrayList<Match> getListMatch(User user) throws Exception {
        ArrayList<Match> listMatch = new ArrayList<>();
        int userId = user.getId();
        String query = "Select * FROM matches Where user1_id = " + userId
                + " or user2_id = " + userId;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Match match = null;
            while (rs.next()) {
                match = new Match();
                match.setId(rs.getInt("id"));
                match.setUser1(getUserById(rs.getInt("user1_id")));
                match.setUser2(getUserById(rs.getInt("user2_id")));
                
                Timestamp timestamp = rs.getTimestamp("startTime");
                LocalDateTime startTime = timestamp.toLocalDateTime();                
                match.setTimeStart(startTime);
                
                match.setScoreUser1(rs.getFloat("scoreUser1"));
                match.setScoreUser2(rs.getFloat("scoreUser2"));
                
                listMatch.add(match);
            }
        } catch (Exception e) {
            throw e;
        }
        return listMatch;
    }
    
    private User getUserById(int userId) throws SQLException {
        User user = null;
        String query = "Select * From users Where id = " + userId;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setAvatarLink(rs.getString("avatarLink"));
                user.setScoreRank(rs.getInt("scoreRank"));
                user.setStatus(rs.getInt("status"));
            }
            
        } catch (SQLException e) {
            throw e;
        }
        return user;
    }
    
}
