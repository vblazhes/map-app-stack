package finki.ukim.mk.map_application.model.auth;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import finki.ukim.mk.map_application.model.User;

import java.util.Objects;

@Entity
public class AuthenticatedUser {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String accessToken;
    private String tokenType;
    @Embedded(prefix = "owner")
    private User user;

    public AuthenticatedUser() {
    }

    public AuthenticatedUser(String accessToken, String tokenType, User user) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.user = user;
    }

    //Getter


    public int getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public User getUser() {
        return user;
    }

    //Setter


    public void setId(int id) {
        this.id = id;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticatedUser that = (AuthenticatedUser) o;
        return Objects.equals(accessToken, that.accessToken) &&
                Objects.equals(tokenType, that.tokenType) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, tokenType, user);
    }

    @Override
    public String toString() {
        return "LoginSuccessful{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", user=" + user +
                '}';
    }
}
