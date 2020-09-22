package finki.ukim.mk.map_application;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.PathUtils;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.Visibility;
import id.zelory.compressor.Compressor;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SaveMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SaveMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaveMapFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MAP_KEY = "MAP_KEY";
    private static final String MODE_KEY = "METHOD_KEY";
    private static final String TAG1 = "SAVE_MAP_FRAGMENT";
    private static final String TAG2 = "UPDATE_MAP_FRAGMENT";


    // TODO: Rename and change types of parameters
    private Map mMap;
    private String mMode;
    private OnFragmentInteractionListener mListener;

    private Uri selectedImage = null;
    private RelativeLayout searchBar;
    private FloatingActionButton fab;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText backgroundEditText;
    private Spinner styleSpinner;
    private Spinner visibilitySpinner;
    private Button saveBtn;
    private Button uploadImgBtn;
    static final int IMG_REQUEST_CODE = 1111;
    private byte [] imgBytes = null;
    private String imgName;
    private boolean imgUploaded = false;

    private String[] styles = new String[]{
            "mapbox://styles/vblazhes/ck62e0e7k0z1p1in0fy9cb2ke",
            "mapbox://styles/vblazhes/ck6b9ro1u18ug1ikvbju56691",
            "mapbox://styles/vblazhes/ck6bcgstg2mam1imxrhebxn4p",
            "mapbox://styles/vblazhes/ck8w0c43g25dk1irrkilq7ob6",
            "mapbox://styles/vblazhes/ck8w1r5se2vnn1jsyuh5c58xs",
            "mapbox://styles/vblazhes/ck8w1ujzm2kp81iubitfay8do",
            "mapbox://styles/vblazhes/ck8w1vdtq2of41ila05e0445k",
    };

    private Visibility[] visibilities = new Visibility[]{
            Visibility.PRIVATE,
            Visibility.PUBLIC
    };

    private String map_style = styles[0];
    private Visibility map_visibility = visibilities[0];

    public SaveMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param map Parameter 1.
     * @return A new instance of fragment SaveMap.
     */
    // TODO: Rename and change types and number of parameters
    public static SaveMapFragment newInstance(Map map, String method) {
        SaveMapFragment fragment = new SaveMapFragment();
        Bundle args = new Bundle();
        args.putSerializable(MAP_KEY, map);
        args.putString(MODE_KEY, method);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMap = (Map) getArguments().getSerializable(MAP_KEY);
            mMode= getArguments().getString(MODE_KEY);

            searchBar = Objects.requireNonNull(getActivity()).findViewById(R.id.search_bar);
            searchBar.setVisibility(View.INVISIBLE);

            fab = getActivity().findViewById(R.id.save_map_fab);
            fab.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save_map, container, false);

        nameEditText = view.findViewById(R.id.mapName_editText);
        descriptionEditText = view.findViewById(R.id.mapDescription_editText);
        backgroundEditText = view.findViewById(R.id.mapBackground_editText);

        //Style Spinner
        styleSpinner = view.findViewById(R.id.mapStyle_spinner);
        ArrayAdapter<CharSequence> adapter_style = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.map_style, android.R.layout.simple_spinner_item);
        adapter_style.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        styleSpinner.setAdapter(adapter_style);
        styleSpinner.setOnItemSelectedListener(this);

        //Visibility Spinner
        visibilitySpinner = view.findViewById(R.id.mapVisibility_spinner);
        ArrayAdapter<CharSequence> adapter_visibility = ArrayAdapter.createFromResource(getContext(), R.array.map_visibility, android.R.layout.simple_spinner_item);
        adapter_visibility.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        visibilitySpinner.setAdapter(adapter_visibility);
        visibilitySpinner.setOnItemSelectedListener(this);

        if(mMode.equals("EDIT") && mMap != null){
            nameEditText.setText(mMap.getName());
            descriptionEditText.setText(mMap.getDescription());
            backgroundEditText.setText(mMap.getBackground());

            List<String> stylesList = Arrays.asList(styles);
            List<Visibility> visibilityList = Arrays.asList(visibilities);

            styleSpinner.setSelection(stylesList.indexOf(mMap.getStyle()));
            visibilitySpinner.setSelection(visibilityList.indexOf(mMap.getVisibility()));
        }

        uploadImgBtn = view.findViewById(R.id.upload_Image_fragment_btn);
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
//                    // Permission is not granted
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()),
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                        // Show an explanation to the user *asynchronously* -- don't block
//                        // this thread waiting for the user's response! After the user
//                        // sees the explanation, try again to request the permission.
//                        Intent implIntent = new Intent();
//                        implIntent.setAction(Intent.ACTION_PICK);
//                        implIntent.setType("image/*");
//                        String[] mimeTypes = {"image/jpeg", "image/png"};
//                        implIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
//
//                        Intent chooser = Intent.createChooser(implIntent, "Choose desired app!");
//
//                        if(chooser.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null){
//                            startActivityForResult(chooser,IMG_REQUEST_CODE);
//                        }
//                    } else {
//                        // No explanation needed; request the permission
//                        ActivityCompat.requestPermissions(getActivity(),
//                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                1010);
//                    }
//                }else{
//
//                }
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

        saveBtn = view.findViewById(R.id.save_map_fragment_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setName(nameEditText.getText().toString());
                mMap.setDescription(descriptionEditText.getText().toString());
                mMap.setBackground(backgroundEditText.getText().toString());
                mMap.setStyle(map_style);
                mMap.setVisibility(map_visibility);

                if(imgUploaded){
                    mMap.setImageFile(1);
                    mMap.setImageBytes(imgBytes);
                    mMap.setImageName(imgName);
                }else{
                    mMap.setImageFile(0);
                }

                sendBack(mMap);

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void sendBack(Map mapData) {
        if (mListener != null) {
            if(mMode.equals("SAVE")){
                mListener.onFragmentInteraction(TAG1, mapData);
            }else if(mMode.equals("EDIT")){
                mListener.onFragmentInteraction(TAG2, mapData);
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
        fab.setVisibility(View.VISIBLE);
        searchBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.mapStyle_spinner:
                map_style = styles[position];
                break;
            case R.id.mapVisibility_spinner:
                map_visibility = visibilities[position];
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        void onFragmentInteraction(String TAG, Object data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                assert data != null;

                selectedImage = data.getData();
                backgroundEditText.setText(selectedImage.getPath());

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

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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
