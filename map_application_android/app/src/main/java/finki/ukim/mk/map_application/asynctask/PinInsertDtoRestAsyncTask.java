package finki.ukim.mk.map_application.asynctask;

import android.os.AsyncTask;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.model.PinDTO;
import finki.ukim.mk.map_application.network.RestApiClient;
import finki.ukim.mk.map_application.repository.PinRepository;
import retrofit2.Call;

import java.io.IOException;

public class PinInsertDtoRestAsyncTask extends AsyncTask<PinDTO, Void, Pin> {

    private PinRepository pinRepository;

    public PinInsertDtoRestAsyncTask(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    @Override
    protected Pin doInBackground(PinDTO... pins) {
        int map_id = pins[0].getMap_id();
        Pin newPin = pins[0].getPin();
//        Call<Pin> insertedPin = RestApiClient.getService().insertPin(map_id, newPin.getLatitude(),
//                newPin.getLongitude(), newPin.getName(), newPin.getDescription(), newPin.getImage());
        newPin.setMap_id(map_id);
        Call<Pin> insertedPin = RestApiClient.getService().insertPin(map_id,newPin);

        try {
             Pin pin = insertedPin.execute().body();
            assert pin != null;
            pin.setMap_id(map_id);
            return pin;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Pin pin) {
        super.onPostExecute(pin);
        pinRepository.insert(pin);
    }
}

