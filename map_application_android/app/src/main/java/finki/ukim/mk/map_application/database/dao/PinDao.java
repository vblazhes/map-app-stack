package finki.ukim.mk.map_application.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import finki.ukim.mk.map_application.model.Pin;

import java.util.List;

@Dao
public interface PinDao {

    //MNC
    @Query("SELECT * FROM Pin")
    LiveData<List<Pin>> getAllMapPins();

    @Query("SELECT * FROM Pin WHERE map_id = :map_id")
    LiveData<List<Pin>> getAllMapPinsByMapID(int map_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPin(Pin pin);

    @Update
    void updatePin(Pin pin);

    @Delete
    void deletePin(Pin pin);

    @Query("DELETE FROM Pin")
    void deleteAllPins();

    @Query("DELETE FROM Pin WHERE map_id = :map_id")
    void deleteAllPinsByMapID(int map_id);
}
