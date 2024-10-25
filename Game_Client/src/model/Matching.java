package model;

import java.io.Serializable;

public class Matching implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private User user1, user2;
    private int status1, status2;
    private int answer1, answer2;
    private int statusMatch;
    private float scoreUser1, scoreUser2;
    private int idQuestionNow;
    private static int cnt = 0;

    public Matching() {
    }

    public Matching(int id, User user1, User user2, int status1, int status2, int answer1, int answer2, int statusMatch, float scoreUser1, float scoreUser2, int idQuestionNow) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.status1 = status1;
        this.status2 = status2;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.statusMatch = statusMatch;
        this.scoreUser1 = scoreUser1;
        this.scoreUser2 = scoreUser2;
        this.idQuestionNow = idQuestionNow;
    }

    public int getAnswer1() {
        return answer1;
    }

    public void setAnswer1(int answer1) {
        this.answer1 = answer1;
    }

    public int getAnswer2() {
        return answer2;
    }

    public void setAnswer2(int answer2) {
        this.answer2 = answer2;
    }

    public int getIdQuestionNow() {
        return idQuestionNow;
    }

    public void setIdQuestionNow(int idQuestionNow) {
        this.idQuestionNow = idQuestionNow;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public float getScoreUser1() {
        return scoreUser1;
    }

    public void setScoreUser1(float scoreUser1) {
        this.scoreUser1 = scoreUser1;
    }

    public float getScoreUser2() {
        return scoreUser2;
    }

    public void setScoreUser2(float scoreUser2) {
        this.scoreUser2 = scoreUser2;
    }

    public static int getCnt() {
        return cnt;
    }

    public static void setCnt(int cnt) {
        Matching.cnt = cnt;
    }

    public int getStatus1() {
        return status1;
    }

    public void setStatus1(int status1) {
        this.status1 = status1;
    }

    public int getStatus2() {
        return status2;
    }

    public void setStatus2(int status2) {
        this.status2 = status2;
    }

    public int getStatusMatch() {
        return statusMatch;
    }

    public void setStatusMatch(int statusMatch) {
        this.statusMatch = statusMatch;
    }
  
}
