package finki.ukim.mk.map_application;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import finki.ukim.mk.map_application.model.User;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.viewmodel.UserProfileViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserProfileNewActivity extends AppCompatActivity implements onMapItemClickListener
        , onMapItemDeleteBtnClickListener
        , onMapItemEditBtnClickListener
        , DeleteAlertDialog.DeleteAlertDialogListener {

    private RecyclerView recyclerView;
    private UserProfileRecyclerViewAdapter adapter;
    private UserProfileViewModel viewModel;
    private AuthenticatedUser authenticatedUser;
    private List<Map> user_maps = new ArrayList<>();
    private Toolbar toolbar;
    private Menu menu;
    private FloatingActionButton addMap_fab;

    //Profile Views
    TextView userFirstLastNameTv;
    TextView userUsernameTv;
    TextView mapsNumberTv;

    private final static String TAG_USER_MAPS = "USER_MAPS_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_new);

        initView();
        initData();
    }

    private void initView() {
        recyclerView = findViewById(R.id.user_recycler_view_map);
        //Horizontal
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //Vertical
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new UserProfileRecyclerViewAdapter(this, this, this);
        recyclerView.setAdapter(adapter);

        this.toolbar = findViewById(R.id.toolbar);
        this.toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);

        addMap_fab = findViewById(R.id.add_map_fab);
        addMap_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateNewMapActivity.class);
                startActivity(intent);
            }
        });

        userFirstLastNameTv = findViewById(R.id.tvUserFirstLastName);
        userUsernameTv = findViewById(R.id.tvUserName);
        mapsNumberTv = findViewById(R.id.tvMapNum);

    }

    private void initData() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(UserProfileViewModel.class);

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

                    Integer mapsNum = user_maps.size();
                    mapsNumberTv.setText(mapsNum.toString());
                }else{
                    mapsNumberTv.setText("0");
                }
            }
        });

        userFirstLastNameTv.setText(authenticatedUser.getUser().getName());
        userUsernameTv.setText(authenticatedUser.getUser().getUsername());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);

        menu.findItem(R.id.menu_item_login).setVisible(false);
        menu.findItem(R.id.menu_item_register).setVisible(false);
        menu.findItem(R.id.menu_item_create_new_map).setVisible(false);
        menu.findItem(R.id.menu_item_user_maps).setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

//        if (id == R.id.menu_item_create_new_map) {
//            Intent intent = new Intent(getApplicationContext(), CreateNewMapActivity.class);
//            startActivity(intent);
//        }
        if (id == R.id.menu_item_explore) {
            Intent intent = new Intent(getApplicationContext(), ExploreMapActivity.class);
            startActivity(intent);
        }
        if (id == R.id.menu_item_user_maps) {
//            openProfileDetailsFragment(authenticatedUser.getUser());
            openUserMapsFragment();
        }
        if (id == R.id.menu_item_logout) {
            onLogout();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapItemClick(int position) {
        Map clickedMap = user_maps.get(position);

        Intent intent = new Intent(getApplicationContext(), ViewMapActivity.class);
        intent.putExtra("map", clickedMap);
        startActivity(intent);
    }


    @Override
    public void onMapItemEditBtnClick(int position) {
        Map clickedMap = user_maps.get(position);

        Intent intent = new Intent(getApplicationContext(), EditMapActivity.class);
        intent.putExtra("map", clickedMap);
        startActivity(intent);
    }

    @Override
    public void onMapItemDeleteBtnClick(int position) {
        DeleteAlertDialog dialog = new DeleteAlertDialog().newInstance(position);
        dialog.show(getSupportFragmentManager(), "DELETE_DIALOG");
    }

    @Override
    public void onYesClicked(int position) {
        viewModel.deleteMap(user_maps.get(position));
    }

    private void onLogout() {
        viewModel.deleteAllAuthUsers();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

    private void openUserMapsFragment() {
        UserMapsFragment fragment = UserMapsFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container, fragment, TAG_USER_MAPS).commit();
    }
}