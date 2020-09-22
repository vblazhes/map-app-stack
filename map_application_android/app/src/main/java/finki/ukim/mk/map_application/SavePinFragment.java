package finki.ukim.mk.map_application;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import finki.ukim.mk.map_application.model.Pin;
import id.zelory.compressor.Compressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SavePinFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SavePinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavePinFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PIN_KEY = "PIN_KEY";
    private static final String MODE_KEY = "MODE";
    private static final String TAG1 = "SAVE_PIN_FRAGMENT";
    private static final String TAG2 = "EDIT_PIN_FRAGMENT";
    private boolean pinSaved = false;


    // TODO: Rename and change types of parameters
    private Pin mPin;
    private String mMode;
    private OnFragmentInteractionListener mListener;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText imageEditText;
    private Button saveButton;
    private Button uploadButton;
    private Toolbar toolbar;
    private RelativeLayout searchBar;
    static final int IMG_REQUEST_CODE = 1111;

    private byte [] imgBytes = null;
    private String imgName;
    private boolean imgUploaded = false;
    private Uri selectedImage = null;


    private CardView edit_cardView;
    private CardView save_cardView;

    public SavePinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pin Parameter 1.
     * @param mode Parameter 2.
     * @return A new instance of fragment SavePinFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavePinFragment newInstance(Pin pin, String mode) {
        SavePinFragment fragment = new SavePinFragment();
        Bundle args = new Bundle();
        args.putSerializable(PIN_KEY, pin);
        args.putString(MODE_KEY, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPin = (Pin) getArguments().getSerializable(PIN_KEY);
            mMode = getArguments().getString(MODE_KEY);
        }

        edit_cardView = Objects.requireNonNull(getActivity()).findViewById(R.id.clicked_pin_card_view);
        save_cardView = getActivity().findViewById(R.id.dropped_pin_card_view);
        save_cardView.setVisibility(View.INVISIBLE);
        if(mMode.equals("EDIT")) {
            edit_cardView.setVisibility(View.INVISIBLE);
        }

        searchBar = Objects.requireNonNull(getActivity()).findViewById(R.id.search_bar);
        searchBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save_pin, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Save pin");

        nameEditText = view.findViewById(R.id.pinName_editText);
        nameEditText.setText(mPin.getName());
        nameEditText.requestFocus();

        descriptionEditText = view.findViewById(R.id.pinDescription_editText);
        descriptionEditText.setScroller(new Scroller(getContext()));
        descriptionEditText.setMaxLines(5);
        descriptionEditText.setVerticalScrollBarEnabled(true);
        descriptionEditText.setMovementMethod(new ScrollingMovementMethod());

        imageEditText = view.findViewById(R.id.pinImage_editText);
        imageEditText.setScroller(new Scroller(getContext()));
        imageEditText.setMaxLines(1);
        imageEditText.setMovementMethod(new ScrollingMovementMethod());

        if(mMode.equals("EDIT")){
            descriptionEditText.setText(mPin.getDescription());
            imageEditText.setText(mPin.getImage());
        }

        uploadButton = view.findViewById(R.id.uploadPin_fragBtn);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1010);
                    }
                }else{
                    Intent implIntent = new Intent();
                    implIntent.setAction(Intent.ACTION_PICK);
                    implIntent.setType("image/*");
                    String[] mimeTypes = {"image/jpeg", "image/png"};
                    implIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);

                    Intent chooser = Intent.createChooser(implIntent, "Choose desired app!");

                    if(chooser.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null){
                        startActivityForResult(chooser,IMG_REQUEST_CODE);
                    }
                }
            }
        });


        saveButton = view.findViewById(R.id.savePin_fragBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPin.setName(nameEditText.getText().toString());
                mPin.setDescription(descriptionEditText.getText().toString());
                mPin.setImage(imageEditText.getText().toString());
                mPin.setImageUploaded(imgUploaded);

                if(imgUploaded){
                    mPin.setImageFile(imgBytes);
                    mPin.setImageName(imgName);
                }

                sendBack(mPin);

                pinSaved = true;
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void sendBack(Pin pinData) {
        if (mListener != null) {
            if(mMode.equals("SAVE")){
                mListener.onFragmentInteraction(TAG1,pinData);
            }else if(mMode.equals("EDIT")){
                mListener.onFragmentInteraction(TAG2,pinData);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        FloatingActionButton fab = getActivity().findViewById(R.id.save_map_fab);

        if(mMode.equals("SAVE")){
            if(pinSaved){
                save_cardView.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.VISIBLE);
            }else {
                save_cardView.setVisibility(View.VISIBLE);
            }
        }else if(mMode.equals("EDIT")){
            if(pinSaved){
                edit_cardView.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.VISIBLE);
            }else {
                edit_cardView.setVisibility(View.VISIBLE);
            }
        }

        searchBar.setVisibility(View.VISIBLE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String TAG, Object setBackText);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                assert data != null;

                selectedImage = data.getData();
                imageEditText.setText(selectedImage.getPath());

                File file = new File(getRealPathFromURI(selectedImage));

                imgUploaded = true;

                try {
                    File compressedFile = new Compressor(Objects.requireNonNull(getContext())).compressToFile(file);

                    FileInputStream fileInputStream = null;

                    try {
                        imgBytes = new byte[(int) compressedFile.length()];

                        fileInputStream = new FileInputStream(compressedFile);
                        fileInputStream.read(imgBytes);
                    }catch (IOException e){
                        e.printStackTrace();
                    }finally {
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    imgName = selectedImage.getLastPathSegment()+".jpg";
                }catch (Exception ignored){
                    Log.e(TAG, "onActivityResult: compression error");
                }

//                try {
//                    InputStream iStream =   Objects.requireNonNull(getContext()).getContentResolver().openInputStream(selectedImage);
//                    byte[] inputData = getBytes(iStream);
//                    imgBytes = inputData;
//                    imgName = selectedImage.getLastPathSegment()+".jpg";
//                    imgUploaded = true;
//                }catch (Exception e){
//                    Log.d(TAG, "onActivityResult: file reading error");
//                }


            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
