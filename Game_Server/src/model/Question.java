package model;

import java.io.Serializable;

public class Question implements Serializable{    
    private static final long serialVersionUID = 1L;
    private int id;
    private String linking;
    private int type;
    private int answer;

    public Question(int id, String linking, int type, int answer) {
        this.id = id;
        this.linking = linking;
        this.type = type;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLinking() {
        return linking;
    }

    public void setLinking(String linking) {
        this.linking = linking;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", linking='" + linking + '\'' +
                ", type=" + type +
                ", answer='" + answer + '\'' +
                '}';
    }
}
