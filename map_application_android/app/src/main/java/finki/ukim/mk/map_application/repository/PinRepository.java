package finki.ukim.mk.map_application.repository;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import finki.ukim.mk.map_application.asynctask.PinDeleteRestAsyncTask;
import finki.ukim.mk.map_application.asynctask.PinInsertDtoRestAsyncTask;
import finki.ukim.mk.map_application.asynctask.PinLoadAsyncTask;
import finki.ukim.mk.map_application.asynctask.PinUpdateDtoRestAsyncTask;
import finki.ukim.mk.map_application.database.AppDatabase;
import finki.ukim.mk.map_application.database.dao.PinDao;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.model.PinDTO;

import java.util.List;

public class PinRepository {

    private PinDao pinDao;

    public PinRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        pinDao = database.pinDao();
    }

    public PinDao getPinDao() {
        return pinDao;
    }


    public LiveData<List<Pin>> getAllMapPins(int map_id) {
        new PinLoadAsyncTask(this).execute(map_id);
        return pinDao.getAllMapPinsByMapID(map_id);
    }

    public void insert(Pin pin) {
        new InsertPinAsyncTask(pinDao).execute(pin);
    }

    public void insertPinRest(PinDTO pinDto) {
        new PinInsertDtoRestAsyncTask(this).execute(pinDto);
    }

    public void update(Pin pin) {
        new UpdatePinAsyncTask(pinDao).execute(pin);
    }

    public void updatePinRest(PinDTO pinDto){
        new PinUpdateDtoRestAsyncTask(this).execute(pinDto);
    }

    public void delete(Pin pin) {
        new DeletePinAsyncTask(pinDao).execute(pin);
    }

    public void deletePinRest(Pin pin){
        new PinDeleteRestAsyncTask(this).execute(pin);
    }

    public void deleteAll() {
        new DeleteAllPinAsyncTask(pinDao).execute();
    }

    public void deleteAllByMapID(int map_id){
        new DeleteAllPinByMapIDAsyncTask(pinDao).execute(map_id);
    }


    /**
     * AsyncTask for repository DB operations follow
     * DB Operations are not allowed to be executed on the main thread it could freeze our app or potentially crash
     * We will use an async task in order to execute this DB operations on Background thread
     * Async task it has to be static so it doesnt have ref to the repository itself
     * otherwise this could cause memory leak
     **/

    private static class InsertPinAsyncTask extends AsyncTask<Pin, Void, Void> {
        private PinDao pinDao;

        private InsertPinAsyncTask(PinDao pinDao) {
            this.pinDao = pinDao;
        }

        @Override
        protected Void doInBackground(Pin... pins) {
            pinDao.insertPin(pins[0]);
            return null;
        }
    }


    private static class UpdatePinAsyncTask extends AsyncTask<Pin, Void, Void> {
        private PinDao pinDao;

        private UpdatePinAsyncTask(PinDao pinDao) {
            this.pinDao = pinDao;
        }

        @Override
        protected Void doInBackground(Pin... pins) {
            pinDao.updatePin(pins[0]);
            return null;
        }
    }

    private static class DeletePinAsyncTask extends AsyncTask<Pin, Void, Void> {
        private PinDao pinDao;

        private DeletePinAsyncTask(PinDao pinDao) {
            this.pinDao = pinDao;
        }

        @Override
        protected Void doInBackground(Pin... pins) {
            pinDao.deletePin(pins[0]);
            return null;
        }
    }

    private static class DeleteAllPinAsyncTask extends AsyncTask<Void, Void, Void> {
        private PinDao pinDao;

        private DeleteAllPinAsyncTask(PinDao pinDao) {
            this.pinDao = pinDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            pinDao.deleteAllPins();
            return null;
        }
    }

    private static class DeleteAllPinByMapIDAsyncTask extends AsyncTask<Integer, Void, Void> {
        private PinDao pinDao;

        private DeleteAllPinByMapIDAsyncTask(PinDao pinDao) {
            this.pinDao = pinDao;
        }


        @Override
        protected Void doInBackground(Integer... integers) {
            pinDao.deleteAllPinsByMapID(integers[0]);
            return null;
        }
    }

}
