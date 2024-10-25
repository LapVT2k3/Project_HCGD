package model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author ADMIN
 */
public class Match implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private User user1, user2;
    private LocalDateTime timeStart;
    private float scoreUser1, scoreUser2;

    public Match() {
    }

    public Match(int id, User user1, User user2, LocalDateTime timeStart, int scoreUser1, int scoreUser2) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.timeStart = timeStart;
        this.scoreUser1 = scoreUser1;
        this.scoreUser2 = scoreUser2;
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

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
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

    @Override
    public String toString() {
        return "Match{" + "id=" + id + ", user1=" + user1 + ", user2=" + user2 + ", timeStart=" + timeStart + ", scoreUser1=" + scoreUser1 + ", scoreUser2=" + scoreUser2 + '}';
    }
    
    
    
}
