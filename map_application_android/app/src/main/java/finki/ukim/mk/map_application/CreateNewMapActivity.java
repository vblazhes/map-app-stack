package finki.ukim.mk.map_application;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.model.PinDTO;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.viewmodel.CreateNewMapViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class CreateNewMapActivity extends FragmentActivity
        implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        SavePinFragment.OnFragmentInteractionListener,
        SaveMapFragment.OnFragmentInteractionListener,
        GoogleMap.OnMarkerClickListener {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String TAG = "Create Map Activity";
    private AuthenticatedUser authenticatedUser;

    private CreateNewMapViewModel viewModel;

    //DropMarkerWindow Info Views
    private CardView cardView;
    private TextView droppedPinTextView;
    private TextView nearTextView;
    private TextView latlngTextView;
    private Button saveMarkerButton;
    private Button discardMarkerButton;

    //ClickedMarkerWindow InfoViews
    private CardView clickedPinCardView;
    private TextView clickedPinTextView;
    private TextView latlangCpTextView;
    private Button btn_details;
    private Button btn_edit;
    private Button btn_delete;


    //Fragments
    private FrameLayout fragmentContainer;
    private FloatingActionButton savedPinFab;

    // Activity objects
    private GoogleMap mMap;
    private List<Marker> map_markers = new ArrayList<>();
    private Marker selectedMarker;
    private List<Pin> saved_pins = new ArrayList<>();
    private int marker_num = 1;

    //Basic building objects of this activity
    private Pin createdPin;
    private Pin editedPin;
    private Map createdMap;
    private boolean clickedPin = false;
    private boolean droppedPin = false;

    //Search bar
    private EditText mSearchBar;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean locationPermissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(CreateNewMapViewModel.class);

        initLayoutView();
        initLayoutListeners();

        // Positioning the location button
        View locationButton = ((View) Objects.requireNonNull(mapFragment.getView()).findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 220, 180, 0);

        try {
            getAuthUser();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        enableMyLocationPermission();


        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);


        //Custom Info Window

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            View infoWindow = getLayoutInflater().inflate(R.layout.marker_custom_info_window, null);


            private void renderInfoWindow(Marker marker, View infoWindow) {
                ImageView markerImage = infoWindow.findViewById(R.id.marker_imageView);
                TextView markerName = infoWindow.findViewById(R.id.markerName_textView);

                //Getting pinData

                if (marker.getTitle() != null && !marker.getTitle().equals("")) {
                    markerName.setText(marker.getTitle());
                } else {
                    markerName.setText("Marker " + marker_num);
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
                if(map_markers.size() == saved_pins.size()){
                    renderInfoWindow(marker, infoWindow);
                    return infoWindow;
                }else {
                    Picasso.with(getApplicationContext()).load("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/600px-No_image_available.svg.png").into((ImageView) infoWindow.findViewById(R.id.marker_imageView));
                    return infoWindow;
                }
            }
        });

    }


    /**
     * CODE FOR ENABLING MY LOCATION
     **/

    private void enableMyLocationPermission() {
        Log.d(TAG, "enableLocationPermission: getting location permissions");
        String[] permissions = {COARSE_LOCATION, FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    Log.d(TAG, "enableMyLocationPermission: Map is not initialized");
                }
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");

        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            Log.d(TAG, "onRequestPermissionsResult: REQUEST_CODE permission denied!");
            return;
        }

        Log.d(TAG, "onRequestPermissionsResult: grantResults.length:" + grantResults.length);

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                locationPermissionDenied = true;
                Log.d(TAG, "onRequestPermissionsResult: permission denied!" + grantResults[i]);
                return;
            }
        }

        // Enable the my location layer if the permission has been granted.
        Log.d(TAG, "onRequestPermissionsResult: permission granted");
        enableMyLocationPermission();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current Location:\nLatitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.d(TAG, "onMapLongClick: Marker dropped!");

        if (selectedMarker != null && !clickedPin) {
            discardMarker();
        } else if (selectedMarker != null && map_markers.size() != saved_pins.size()) {
            discardMarker();
        } else if (selectedMarker !=null && clickedPin) {
            selectedMarker.hideInfoWindow();
            hideClickedMarkerWindow();
        }

        Geocoder geocoder = new Geocoder(CreateNewMapActivity.this);
        List<Address> addresses = new ArrayList<>();

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address address = addresses.get(0);

        selectedMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude))
                .title(address.getLocality()));
        map_markers.add(selectedMarker);
        showDropMarkerWindow(address);

        droppedPin = true;
    }

    public void initLayoutView() {
        //Dropped Marker Views

        cardView = findViewById(R.id.dropped_pin_card_view);
        cardView.setVisibility(View.INVISIBLE);
        droppedPinTextView = findViewById(R.id.droppedPin_text_view);
        nearTextView = findViewById(R.id.near_text_view);
        latlngTextView = findViewById(R.id.latlng_text_view);
        saveMarkerButton = findViewById(R.id.btn_saveMarker);
        discardMarkerButton = findViewById(R.id.btn_discardMarker);

        fragmentContainer = findViewById(R.id.fragment_container);
        savedPinFab = findViewById(R.id.save_map_fab);

        //Clicked Marker Views
        clickedPinCardView = findViewById(R.id.clicked_pin_card_view);
        clickedPinCardView.setVisibility(View.INVISIBLE);
        clickedPinTextView = findViewById(R.id.clicked_pin_text_view);
        latlangCpTextView = findViewById(R.id.latlng_clickedPin_text_view);
        btn_delete = findViewById(R.id.btn_deleteMarker);
        btn_details = findViewById(R.id.btn_detailsMarker);
        btn_edit = findViewById(R.id.btn_editMarker);

        //SearchBar
        mSearchBar = (EditText) findViewById(R.id.pin_search_etInput);
        mSearchBar.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

    }

    public void showDropMarkerWindow(Address address) {
        savedPinFab.setVisibility(View.INVISIBLE);
        cardView.setVisibility(View.VISIBLE);

        String nearTextViewContent;
        if (address.getLocality() != null) {
            nearTextViewContent = "Near " + address.getLocality();
        } else {
            nearTextViewContent = "Unknown location";
        }
        nearTextView.setText(nearTextViewContent);

        String latlngTextViewContent = " (" + address.getLatitude() + ", " + address.getLongitude() + ")";
        latlngTextView.setText(latlngTextViewContent);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (droppedPin) {
            droppedPin = false;
            discardMarker();
        } else if (clickedPin) {
            clickedPin = false;
            hideClickedMarkerWindow();
        }
    }

    public void discardMarker() {
        Log.d(TAG, "discardMarker: called");

        cardView.setVisibility(View.INVISIBLE);
        savedPinFab.setVisibility(View.VISIBLE);
        if (selectedMarker != null) {
            map_markers.remove(selectedMarker);
            selectedMarker.remove();
        }

        selectedMarker = null;

        Log.d(TAG, "discardMarker: savedPins:" + saved_pins.size());
        Log.d(TAG, "discardMarker: savedMarkers:" + map_markers.size());

    }

    public void initLayoutListeners() {
        discardMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discardMarker();
            }
        });

        saveMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createdPin = new Pin();
                createdPin.setLatitude(selectedMarker.getPosition().latitude);
                createdPin.setLongitude(selectedMarker.getPosition().longitude);
                createdPin.setName("Marker " + marker_num);

                openSavePinFragment(createdPin, "SAVE");
            }
        });

        savedPinFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (createdMap == null) {
                    createdMap = new Map();
                }
                createdMap.setCenter_latitude(mMap.getCameraPosition().target.latitude);
                createdMap.setCenter_longitude(mMap.getCameraPosition().target.longitude);
                createdMap.setDefault_zoom((int) mMap.getCameraPosition().zoom);

                openSaveMapFragment(createdMap, "SAVE");
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = map_markers.indexOf(selectedMarker);
                editedPin = saved_pins.get(id);
                openSavePinFragment(editedPin, "EDIT");
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = map_markers.indexOf(selectedMarker);
                Marker marker = selectedMarker;
                saved_pins.remove(id);
                map_markers.remove(id);
                marker.remove();

                hideClickedMarkerWindow();

            }
        });

        mSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute place searching method

                    geoLocate();
                }

                return false;
            }
        });

    }

    public void geoLocate(){

//        String searchString = mSearchBar.getText().toString();
//
//        Geocoder geocoder = new Geocoder(CreateNewMapActivity.this);
//        List<Address> places = new ArrayList<>();
//
//        try {
//            places = geocoder.getFromLocationName(searchString, 1);
//        }catch (IOException e){
//            Log.e(TAG, "geoLocate: IOException "+e.getMessage());
//        }
//
//        if(places.size() > 0){
//            Address address = places.get(0);
//            Log.d(TAG, "geoLocate: found a location: "+address.toString());
//        }else {
//            Toast.makeText(this, "No place found!", Toast.LENGTH_SHORT).show();
//        }

        droppedPin = true;

        if (selectedMarker != null && !clickedPin) {
            discardMarker();
        } else if (selectedMarker != null && map_markers.size() != saved_pins.size()) {
            discardMarker();
        } else if (clickedPin) {
            assert selectedMarker != null;
            selectedMarker.hideInfoWindow();
            hideClickedMarkerWindow();
        }

        String searchString = mSearchBar.getText().toString();

        Geocoder geocoder = new Geocoder(CreateNewMapActivity.this);
        List<Address> places = new ArrayList<>();

        try {
            places = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException " + e.getMessage());
        }

        if (places.size() > 0) {
            Address address = places.get(0);
            Log.d(TAG, "geoLocate: found a location - Marker dropped: " + address.toString());

            selectedMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(address.getLatitude(), address.getLongitude()))
                    .title(address.getLocality()));
            map_markers.add(selectedMarker);

            LatLng viewPort = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(viewPort, mMap.getCameraPosition().zoom), 2000, null);

            showDropMarkerWindow(address);

        } else {
            Toast.makeText(this, "No place found!", Toast.LENGTH_SHORT).show();
        }

    }


    public void openSavePinFragment(Pin createdPin, String mode) {

        SavePinFragment fragment = SavePinFragment.newInstance(createdPin, mode);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.addToBackStack(null);
        if (mode.equals("SAVE"))
            transaction.add(R.id.fragment_container, fragment, "SAVE_PIN_FRAGMENT").commit();
        else
            transaction.add(R.id.fragment_container, fragment, "EDIT_PIN_FRAGMENT").commit();

    }

    public void openSaveMapFragment(Map createdMap, String mode) {
        SaveMapFragment fragment = SaveMapFragment.newInstance(createdMap, mode);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container, fragment, "SAVE_MAP_FRAGMENT").commit();
    }

    @Override
    public void onFragmentInteraction(String TAG, Object data) {

        switch (TAG) {
            case "SAVE_PIN_FRAGMENT":
                createdPin = (Pin) data;
                selectedMarker.setTitle(createdPin.getName());
                selectedMarker.setSnippet(createdPin.getImage());

                if (createdPin.getName().equals("Marker " + marker_num)) {
                    marker_num++;
                }
                nearTextView.setText(createdPin.getName());
                saved_pins.add(createdPin);
                selectedMarker = null;
                droppedPin = false;
                onBackPressed();
                Log.d(TAG, "onFragmentInteraction: savedPin: " + saved_pins.size());
                Log.d(TAG, "onFragmentInteraction: savedMarkers" + map_markers.size());
                Toast.makeText(this, "Marker saved!", Toast.LENGTH_SHORT).show();
                break;
            case "SAVE_MAP_FRAGMENT":
                createdMap = (Map) data;
                onBackPressed();
                Log.d(TAG, "onFragmentInteraction: savedMap:" + createdMap.getName() + " " + createdMap.getDescription());
                viewModel.insert(createdMap, saved_pins, authenticatedUser);
                Toast.makeText(this, "Map saved!", Toast.LENGTH_SHORT).show();
                break;
            case "EDIT_PIN_FRAGMENT":
                editedPin = (Pin) data;
                selectedMarker.setTitle(editedPin.getName());
                selectedMarker.setSnippet(editedPin.getImage());
                selectedMarker.hideInfoWindow();

                clickedPinTextView.setText(editedPin.getName());
                selectedMarker = null;
                onBackPressed();
                Toast.makeText(this, "Marker updated!", Toast.LENGTH_SHORT).show();
        }

    }

    // Additional functionality code handles edit saved pin on map

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (map_markers.size() == saved_pins.size()) {
            clickedPin = true;
            selectedMarker = marker;
            showClickedMarkerWindow(marker);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), mMap.getCameraPosition().zoom), 1000, null);
        }

        return false;
    }


    public void showClickedMarkerWindow(Marker marker) {

        savedPinFab.setVisibility(View.INVISIBLE);

        selectedMarker = marker;

        clickedPinTextView.setText(marker.getTitle());
        String location = "lat: " + marker.getPosition().latitude + ", lng: " + marker.getPosition().longitude;
        latlangCpTextView.setText(location);

        clickedPinCardView.setVisibility(View.VISIBLE);

    }

    public void hideClickedMarkerWindow() {
        selectedMarker = null;
        clickedPinCardView.setVisibility(View.INVISIBLE);
        savedPinFab.setVisibility(View.VISIBLE);
    }

    public void getAuthUser() throws ExecutionException, InterruptedException {
        AuthenticatedUser authUser = viewModel.getAuthUser();
        if(authUser!= null){
            authenticatedUser = authUser;
        }
    }

}
