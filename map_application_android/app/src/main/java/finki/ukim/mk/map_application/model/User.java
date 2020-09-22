package finki.ukim.mk.map_application.model;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User implements Serializable {
    @PrimaryKey
    private Long id;

    @NonNull
    @Size(max = 40)
    private String name;

    @NonNull
    @Size(max = 15)
    private String username;

    @NonNull
    @Size(max = 40)
    private String email;

    @NonNull
    @Size(max = 100)
    private String password;

    @Embedded(prefix = "role_")
    private Role role;

    public User(){}

    public User(Long id, @NonNull String name, @NonNull String username, @NonNull String email, @NonNull String password, Role role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //Getters
    public Long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    //Setters


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
