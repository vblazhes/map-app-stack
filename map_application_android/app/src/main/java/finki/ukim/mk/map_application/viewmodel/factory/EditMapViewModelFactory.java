package finki.ukim.mk.map_application.viewmodel.factory;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import finki.ukim.mk.map_application.viewmodel.EditMapViewModel;

public class EditMapViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private int mMapID;


    public EditMapViewModelFactory(Application application, int MapID) {
        mApplication = application;
        mMapID = MapID;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new EditMapViewModel(mApplication, mMapID);
    }

}
