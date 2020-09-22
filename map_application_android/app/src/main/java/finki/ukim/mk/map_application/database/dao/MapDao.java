package finki.ukim.mk.map_application.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.Visibility;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Dao
public interface MapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMap(Map map);

    @Update
    void updateMap(Map map);

    @Delete
    void deleteMap(Map map);

    @Query("DELETE FROM Map")
    void deleteAllMaps();

    @Query("SELECT * FROM Map")
    LiveData<List<Map>> getAllMaps();

    @Query("SELECT MAX(last_update) FROM Map")
    Long lastUpdated();

    @Query("UPDATE Map SET last_update = :time")
    void updateLoadTimeOfRecords(Long time);

    @Query("SELECT * FROM Map")
    int hasEntries();

    @Query("SELECT * FROM Map WHERE id = :id")
    LiveData<Map> getMapById(int id);

    @Query("SELECT * FROM Map WHERE owner_username = :username")
    LiveData<List<Map>> getAllUsersMaps(String username);

    @Query("SELECT * FROM Map WHERE approved = 1 AND visibility = 'PUBLIC'")
    LiveData<List<Map>> getApprovedMaps();
}
