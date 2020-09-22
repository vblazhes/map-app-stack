package finki.ukim.mk.map_application.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import finki.ukim.mk.map_application.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM User WHERE username = :username")
    LiveData<User> getUserByUsername(String username);

    @Insert
    void insertUser(User user);

    @Update
    void updateUSer(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM User")
    void deleteAllUsers();
}
