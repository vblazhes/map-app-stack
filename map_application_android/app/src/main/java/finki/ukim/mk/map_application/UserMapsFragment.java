package finki.ukim.mk.map_application;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import finki.ukim.mk.map_application.adapter.UserProfileRecyclerViewAdapter;
import finki.ukim.mk.map_application.adapter.listener.onMapItemClickListener;
import finki.ukim.mk.map_application.adapter.listener.onMapItemDeleteBtnClickListener;
import finki.ukim.mk.map_application.adapter.listener.onMapItemEditBtnClickListener;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.viewmodel.UserProfileViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserMapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserMapsFragment extends Fragment implements onMapItemClickListener
        , onMapItemDeleteBtnClickListener
        , onMapItemEditBtnClickListener
        , DeleteAlertDialog.DeleteAlertDialogListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //View items
    private UserProfileViewModel viewModel;
    private RecyclerView recyclerView;
    private UserProfileRecyclerViewAdapter adapter;
    private List<Map> user_maps = new ArrayList<>();
    private AuthenticatedUser authenticatedUser;
//    private FloatingActionButton addMap_fab;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserMapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserMapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserMapsFragment newInstance() {
        UserMapsFragment fragment = new UserMapsFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_maps, container, false);
        recyclerView = view.findViewById(R.id.user_recycler_view_map);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new UserProfileRecyclerViewAdapter(this, this, this);
        recyclerView.setAdapter(adapter);

//        addMap_fab = view.findViewById(R.id.add_map_fab);
//        addMap_fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), CreateNewMapActivity.class);
//                startActivity(intent);
//            }
//        });

        initData();

        return view;
    }

    private void initData(){
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(Objects.requireNonNull(getActivity()).getApplication()).create(UserProfileViewModel.class);

        try {
            authenticatedUser = viewModel.getAuthUser();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        viewModel.getUserMaps(authenticatedUser.getUser()).observe(this, new Observer<List<Map>>() {
            @Override
            public void onChanged(List<Map> maps) {
                adapter.setMaps(maps);
                if (maps.size() > 0) {
                    user_maps.clear();
                    user_maps.addAll(maps);
                }
            }
        });
    }

    @Override
    public void onMapItemClick(int position) {
        Map clickedMap = user_maps.get(position);

        Intent intent = new Intent(getContext(), ViewMapActivity.class);
        intent.putExtra("map", clickedMap);
        startActivity(intent);
    }


    @Override
    public void onMapItemEditBtnClick(int position) {
        Map clickedMap = user_maps.get(position);

        Intent intent = new Intent(getContext(), EditMapActivity.class);
        intent.putExtra("map", clickedMap);
        startActivity(intent);
    }


    @Override
    public void onMapItemDeleteBtnClick(int position) {
        DeleteAlertDialog dialog = new DeleteAlertDialog().newInstance(position);
        dialog.show(getFragmentManager(), "DELETE_DIALOG");
    }

    @Override
    public void onYesClicked(int position) {
        viewModel.deleteMap(user_maps.get(position));
    }
}