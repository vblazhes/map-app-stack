package finki.ukim.mk.map_application;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import finki.ukim.mk.map_application.adapter.PinRecyclerViewAdapter;
import finki.ukim.mk.map_application.adapter.listener.onPinItemClickListener;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.model.auth.LoginRequest;
import finki.ukim.mk.map_application.viewmodel.PinsListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PinsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PinsListFragment extends Fragment implements onPinItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PIN_KEY = "PIN_KEY";
    private static final String TAG = "PIN_FRAGMENT";

    // TODO: Rename and change types of parameters
    private PinsListFragment.OnFragmentInteractionListener mListener;
    private int mMap_id;
    private RecyclerView recyclerView;
    private PinRecyclerViewAdapter adapter;
    private PinsListViewModel viewModel;
    private List<Pin> pins_data = new ArrayList<>();
    private Toolbar toolbar;
    private FloatingActionButton fab_details;

    public PinsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PinsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PinsListFragment newInstance(int param1) {
        PinsListFragment fragment = new PinsListFragment();
        Bundle args = new Bundle();
        args.putInt(PIN_KEY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMap_id = getArguments().getInt(PIN_KEY);
        }

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PinsListViewModel.class);
        this.fab_details = getActivity().findViewById(R.id.pins_list_fab);
        fab_details.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pins_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_pins);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new PinRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        viewModel.getMapPins(mMap_id).observe(this, new Observer<List<Pin>>() {
            @Override
            public void onChanged(List<Pin> pins) {
                adapter.setPins(pins_data);
                if(pins!=null && pins.size()>0){
                    pins_data.clear();
                    pins_data.addAll(pins);
                }
            }
        });

        return view;
    }

    @Override
    public void onPinItemClick(int position) {
        try {
            onButtonPressed(position);
            Log.d(TAG, "onPinItemClick: "+position);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int position) throws ExecutionException, InterruptedException {

        if (mListener != null) {
            Log.d(TAG, "onButtonPressed: pressed btn"+position);
            mListener.onFragmentInteraction(TAG, position);
        } else {
            Toast.makeText(getContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PinsListFragment.OnFragmentInteractionListener) {
            mListener = (PinsListFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        fab_details.setVisibility(View.VISIBLE);
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
        void onFragmentInteraction(String TAG, int postion);
    }
}