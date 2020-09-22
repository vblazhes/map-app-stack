package finki.ukim.mk.map_application;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import finki.ukim.mk.map_application.adapter.PlaceAutocompleteAdapter;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.model.PinDTO;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.viewmodel.EditMapViewModel;
import finki.ukim.mk.map_application.viewmodel.factory.EditMapViewModelFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EditMapActivity extends FragmentActivity implements OnMapReadyCallback
        , GoogleMap.OnMarkerClickListener
        , GoogleMap.OnMapClickListener
        , GoogleMap.OnMapLongClickListener
        , SaveMapFragment.OnFragmentInteractionListener
        , SavePinFragment.OnFragmentInteractionListener
        , DeleteAlertDialog.DeleteAlertDialogListener {

    private AuthenticatedUser authenticatedUser;
    private GoogleMap mMap;
    private Map mapData;
    private EditMapViewModel viewModel;
    private List<Pin> saved_pins = new ArrayList<>();
    private List<Marker> map_markers = new ArrayList<>();


    //ActivityFunctionalityObjects
    private static final String TAG = "EDIT_MAP_ACTIVITY";
    private Marker selectedMarker;
    private Pin createdPin;
    private Pin editedPin;
    private int marker_num = 1;
    private boolean droppedPin = false;
    private boolean clickedPin = false;


    //Fragments
    private FrameLayout fragmentContainer;
    private FloatingActionButton saveMapFab;

    //DropMarkerWindow Info Views
    private CardView droppedPinCardView;
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

    //Search bar
    private EditText mSearchBar;
    private RelativeLayout searchBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent received_intent = getIntent();
        mapData = (Map) received_intent.getSerializableExtra("map");

        setContentView(R.layout.activity_edit_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        Places.initialize(getApplicationContext(), "AIzaSyA-PaHIJuqXLggzTALa9rGxhpqHKO3LUfk");

        initView();
        initLayoutListeners();
    }

    private void initView() {
        // Dropped Marker Views
        droppedPinCardView = findViewById(R.id.dropped_pin_card_view);
        droppedPinCardView.setVisibility(View.INVISIBLE);
        droppedPinTextView = findViewById(R.id.droppedPin_text_view);
        nearTextView = findViewById(R.id.near_text_view);
        latlngTextView = findViewById(R.id.latlng_text_view);
        saveMarkerButton = findViewById(R.id.btn_saveMarker);
        discardMarkerButton = findViewById(R.id.btn_discardMarker);

        fragmentContainer = findViewById(R.id.fragment_container);
        saveMapFab = findViewById(R.id.save_map_fab);

        //Clicked Marker Views
        clickedPinCardView = findViewById(R.id.clicked_pin_card_view);
        clickedPinCardView.setVisibility(View.INVISIBLE);
        clickedPinTextView = findViewById(R.id.clicked_pin_text_view);
        latlangCpTextView = findViewById(R.id.latlng_clickedPin_text_view);
        btn_delete = findViewById(R.id.btn_deleteMarker);
        btn_details = findViewById(R.id.btn_detailsMarker);
        btn_edit = findViewById(R.id.btn_editMarker);

        //SearchBar
        mSearchBar = findViewById(R.id.pin_search_etInput);
//        mSearchBar.setFocusable(false);
        mSearchBar.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchBarLayout = findViewById(R.id.search_bar);

    }

    private void initData() {
        viewModel = new ViewModelProvider(this, new EditMapViewModelFactory(getApplication(), mapData.getId())).get(EditMapViewModel.class);

        viewModel.getAllMapPins().observe(this, new Observer<List<Pin>>() {
            @Override
            public void onChanged(List<Pin> pins) {
                if (pins.size() > 0) {
                    saved_pins.clear();
                    saved_pins.addAll(pins);

                    for (Marker marker : map_markers) {
                        marker.remove();
                    }
                    if (map_markers.size() > 0) {
                        map_markers.clear();
                    }

                    for (Pin pin : saved_pins) {
                        LatLng pin_position = new LatLng(pin.getLatitude(), pin.getLongitude());

                        Marker marker = mMap.addMarker(new MarkerOptions().position(pin_position)
                                .title(pin.getName())
                                .snippet(pin.getImage()));
                        map_markers.add(marker);
                    }
                }
            }
        });

        LatLng viewPort = new LatLng(mapData.getCenter_latitude(), mapData.getCenter_longitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(viewPort, 10), 5000, null);

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

        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);

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

        initData();
    }

    /**
     * Set map listeners
     **/

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.d(TAG, "onMapLongClick: Marker dropped!");
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

        Geocoder geocoder = new Geocoder(EditMapActivity.this);
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
    }

    @Override
    public void onMapClick(LatLng latLng) {
        searchBarLayout.setVisibility(View.VISIBLE);
        if (droppedPin) {
            droppedPin = false;
            discardMarker();
        } else if (clickedPin) {
            clickedPin = false;
            hideClickedMarkerWindow();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (map_markers.size() == saved_pins.size()) {
            clickedPin = true;
            selectedMarker = marker;
            showClickedMarkerWindow(marker);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), mMap.getCameraPosition().zoom), 1000, null);
            searchBarLayout.setVisibility(View.INVISIBLE);
        }
        return false;
    }


    //HelperFunctions

    /**
     * Set listeners on map layout items
     **/
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

        saveMapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapData.setCenter_latitude(mMap.getCameraPosition().target.latitude);
                mapData.setCenter_longitude(mMap.getCameraPosition().target.longitude);
                mapData.setDefault_zoom((int) mMap.getCameraPosition().zoom);

                openSaveMapFragment(mapData, "EDIT");
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
                onDeletePinBtnClick(id);
            }
        });

        mSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //execute place searching method

                    geoLocate();

                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchBar.getWindowToken(), 0);
                    return true;
                }

                return false;
            }
        });

//        mSearchBar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Initialize place field list
//                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME);
//                //
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(EditMapActivity.this);
//                startActivityForResult(intent, 100);
//
//            }
//        });

    }

    public void geoLocate() {
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

        Geocoder geocoder = new Geocoder(EditMapActivity.this);
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

    public void showDropMarkerWindow(Address address) {
        saveMapFab.setVisibility(View.INVISIBLE);
        droppedPinCardView.setVisibility(View.VISIBLE);

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

    public void discardMarker() {
        Log.d(TAG, "discardMarker: called");
        droppedPinCardView.setVisibility(View.INVISIBLE);
        saveMapFab.setVisibility(View.VISIBLE);
        if (selectedMarker != null) {
            map_markers.remove(selectedMarker);
            selectedMarker.remove();
        }

        selectedMarker = null;

        Log.d(TAG, "discardMarker: savedPins:" + saved_pins.size());
        Log.d(TAG, "discardMarker: savedMarkers:" + map_markers.size());

    }

    public void hideClickedMarkerWindow() {
        selectedMarker = null;
        clickedPinCardView.setVisibility(View.INVISIBLE);
        saveMapFab.setVisibility(View.VISIBLE);
    }

    public void showClickedMarkerWindow(Marker marker) {
        saveMapFab.setVisibility(View.INVISIBLE);

        selectedMarker = marker;

        clickedPinTextView.setText(marker.getTitle());
        String location = "lat: " + marker.getPosition().latitude + ", lng: " + marker.getPosition().longitude;
        latlangCpTextView.setText(location);

        clickedPinCardView.setVisibility(View.VISIBLE);
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

    public void openSaveMapFragment(Map mapData, String mode) {
        SaveMapFragment fragment = SaveMapFragment.newInstance(mapData, mode);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.addToBackStack(null);
        if (mode.equals("SAVE"))
            transaction.add(R.id.fragment_container, fragment, "SAVE_MAP_FRAGMENT").commit();
        else
            transaction.add(R.id.fragment_container, fragment, "EDIT_MAP_FRAGMENT").commit();
    }

    @Override
    public void onFragmentInteraction(String TAG, Object data) {

        switch (TAG) {
            case "SAVE_PIN_FRAGMENT":
                createdPin = (Pin) data;
                if (createdPin.getName().equals("Marker " + marker_num)) {
                    marker_num++;
                }
                nearTextView.setText(createdPin.getName());
                saved_pins.add(createdPin);
                selectedMarker = null;
                droppedPin = false;
                PinDTO pinDto = new PinDTO(mapData.getId(), createdPin);
                viewModel.insertPin(pinDto);
                onBackPressed();
                Log.d(TAG, "onFragmentInteraction: savedPin: " + saved_pins.size());
                Log.d(TAG, "onFragmentInteraction: savedMarkers" + map_markers.size());
                Toast.makeText(this, "Marker saved!", Toast.LENGTH_SHORT).show();
                break;
            case "UPDATE_MAP_FRAGMENT":
                mapData = (Map) data;
                onBackPressed();
                Log.d(TAG, "onFragmentInteraction: savedMap:" + mapData.getName() + " " + mapData.getDescription());
                viewModel.updateMap(mapData);
                Toast.makeText(this, "Map updated!", Toast.LENGTH_SHORT).show();
                break;
            case "EDIT_PIN_FRAGMENT":
                selectedMarker = null;
                editedPin = (Pin) data;
                clickedPinTextView.setText(editedPin.getName());
                PinDTO pinDTO = new PinDTO(mapData.getId(), editedPin);
                viewModel.updatePin(pinDTO);
//                viewModel.updatePin(pinDTO);
                onBackPressed();
                Toast.makeText(this, "Marker updated!", Toast.LENGTH_SHORT).show();
        }

    }

    private void onDeletePinBtnClick(int id) {
        DeleteAlertDialog dialog = new DeleteAlertDialog().newInstance(id);
        dialog.show(getSupportFragmentManager(), "DELETE_DIALOG");
    }

    @Override
    public void onYesClicked(int position) {
        editedPin = saved_pins.get(position);
        viewModel.deletePin(editedPin);
        clickedPinCardView.setVisibility(View.INVISIBLE);
        saveMapFab.setVisibility(View.VISIBLE);
        if (map_markers.size() == 1) {
            for (Marker marker : map_markers) {
                marker.remove();
            }
            map_markers.clear();
        }
    }


    public void getAuthUser() throws ExecutionException, InterruptedException {
        AuthenticatedUser authUser = viewModel.getAuthUser();
        if (authUser != null) {
            authenticatedUser = authUser;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);

            mSearchBar.setText(place.getAddress());
        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
        }

    }
}
