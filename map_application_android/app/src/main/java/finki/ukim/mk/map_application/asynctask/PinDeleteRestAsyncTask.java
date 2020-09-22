package finki.ukim.mk.map_application.asynctask;

import android.os.AsyncTask;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.network.RestApiClient;
import finki.ukim.mk.map_application.repository.MapRepository;
import finki.ukim.mk.map_application.repository.PinRepository;
import retrofit2.Call;

import java.io.IOException;

public class PinDeleteRestAsyncTask extends AsyncTask<Pin, Void, Pin> {
    private PinRepository pinRepository;

    public PinDeleteRestAsyncTask(PinRepository pinRepository){
        this.pinRepository = pinRepository;
    }


    @Override
    protected Pin doInBackground(Pin... pins) {
        Pin pin = pins[0];
        final Call<Void> deletePin = RestApiClient.getService().deletePin(pin.getId());
        try {
            deletePin.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pin;
    }

    @Override
    protected void onPostExecute(Pin pin) {
        super.onPostExecute(pin);
        pinRepository.delete(pin);
    }
}
