package finki.ukim.mk.map_application.database.dao;

import androidx.room.*;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;

@Dao
public interface AuthenticatedUserDao {

    @Insert
    void insertAuthUser(AuthenticatedUser authenticatedUser);

    @Update
    void updateAuthUser(AuthenticatedUser authenticatedUser);

    @Delete
    void deleteAuthUser(AuthenticatedUser authenticatedUser);

    @Query("SELECT * FROM AuthenticatedUser")
    AuthenticatedUser getAuthenticatedUser();

    @Query("DELETE FROM AuthenticatedUser")
    void deleteAllAuthUsers();
}
