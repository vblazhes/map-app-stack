package finki.ukim.mk.map_application;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import finki.ukim.mk.map_application.adapter.MarkerCustomInfoWindow;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.viewmodel.ViewMapViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewMapActivity extends FragmentActivity implements OnMapReadyCallback,PinsListFragment.OnFragmentInteractionListener {

    private GoogleMap mMap;
    private ViewMapViewModel viewMapViewModel;
    private int MAP_ID;
    private Map map_data = new Map();
    private List<Pin> pins_data = new ArrayList<>();
    private List<Marker> pin_markers = new ArrayList<>();
    private FrameLayout fragmentContainer;
    private FloatingActionButton fab_pins_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent received_intent = getIntent();
        map_data = (Map) received_intent.getSerializableExtra("map");
//        MAP_ID = received_intent.getIntExtra("map_id", 1);
        MAP_ID = map_data.getId();

        setContentView(R.layout.activity_map_view1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fragmentContainer = findViewById(R.id.fragment_container);
        fab_pins_list = findViewById(R.id.pins_list_fab);

        fab_pins_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPinsListFragment();
            }
        });

    }

    private void initData() {

        viewMapViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(ViewMapViewModel.class);

//        viewMapViewModel.getMapById(MAP_ID).observe(this, new Observer<Map>() {
//            @Override
//            public void onChanged(Map map) {
//               map_data = map;
//            }
//        });

        viewMapViewModel.getAllMapPins(map_data.getId()).observe(this, new Observer<List<Pin>>() {
            @Override
            public void onChanged(List<Pin> pins) {
                if (pins.size() > 0) {
                    pins_data.clear();
                    pins_data.addAll(pins);

                    for (Marker marker : pin_markers) {
                        marker.remove();
                    }

                    pin_markers.clear();

                    for (Pin pin : pins_data) {
                        LatLng pin_position = new LatLng(pin.getLatitude(), pin.getLongitude());

                        Marker marker = mMap.addMarker(new MarkerOptions().position(pin_position)
                                .title(pin.getName())
                                .snippet(pin.getImage()));
                        pin_markers.add(marker);
                    }
                }
            }
        });

        System.out.println(map_data.getCenter_latitude());

        LatLng viewPort = new LatLng(map_data.getCenter_latitude(), map_data.getCenter_longitude());
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(viewPort, 10), 5000, null);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(viewPort, map_data.getDefault_zoom()), 5000, null);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            View infoWindow = getLayoutInflater().inflate(R.layout.marker_custom_info_window, null);


            private void renderInfoWindow(Marker marker, View infoWindow){
                ImageView markerImage = infoWindow.findViewById(R.id.marker_imageView);
                TextView markerName = infoWindow.findViewById(R.id.markerName_textView);

                //Getting pinData

                if(!marker.getTitle().equals("")){
                    markerName.setText(marker.getTitle());
                }

                if (marker.getSnippet() != null && !marker.getSnippet().equals("")) {
                    if(marker.getSnippet().startsWith("http")) {
                        Picasso.with(getApplicationContext()).load(marker.getSnippet()).into(markerImage);
                    }else if(marker.getSnippet().startsWith("/images/")){
                        String imgUrl = "http://192.168.1.101:3000/"+marker.getSnippet();
                        Picasso.with(getApplicationContext()).load(imgUrl).into(markerImage);
                    }
                }

            }

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                renderInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

        initData();

    }

    public void openPinsListFragment(){
        PinsListFragment fragment = PinsListFragment.newInstance(MAP_ID);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container, fragment, "PIN_LIST_FRAGMENT").commit();
    }

    @Override
    public void onFragmentInteraction(String TAG, int postion) {
        switch (TAG){
            case "PIN_FRAGMENT":
                Log.d(TAG, "onFragmentInteraction: "+postion);
                if(pin_markers.size() > postion){
                    Marker marker = pin_markers.get(postion);
                    onBackPressed();
                    marker.showInfoWindow();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), mMap.getCameraPosition().zoom), 1000, null);
                }
        }
    }
}
