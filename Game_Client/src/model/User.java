package model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author ADMIN
 */
public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    private int id;
    private String userName;
    private String password;
    private String name;
    private String avatarLink;
    private int scoreRank;
    private int status;
    private static int cnt = 0;

    public User() {
    }

    public User(String userName, String password, String name, String avatarLink, int scoreRank, int status) {
        this.id = ++cnt;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.avatarLink = avatarLink;
        this.scoreRank = scoreRank;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public int getScoreRank() {
        return scoreRank;
    }

    public void setScoreRank(int scoreRank) {
        this.scoreRank = scoreRank;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", userName=" + userName + ", password=" + password + ", name=" + name + ", avatarLink=" + avatarLink + ", scoreRank=" + scoreRank + ", status=" + status + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.avatarLink, other.avatarLink);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
        hash = 67 * hash + Objects.hashCode(this.userName);
        hash = 67 * hash + Objects.hashCode(this.password);
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.avatarLink);
        return hash;
    }
    
    
}
