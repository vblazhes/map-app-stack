package finki.ukim.mk.map_application;

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import finki.ukim.mk.map_application.adapter.MapRecyclerViewAdapter;
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

public class UserProfileActivity extends AppCompatActivity implements onMapItemClickListener
        , onMapItemDeleteBtnClickListener
        , onMapItemEditBtnClickListener
        , DeleteAlertDialog.DeleteAlertDialogListener
        , UserProfileDetailsFragment.OnFragmentInteractionListener {

    private final static String TAG_DETAILS = "DETAILS_FRAGMENT";
    private UserProfileViewModel viewModel;
    private RecyclerView recyclerView;
    private UserProfileRecyclerViewAdapter adapter;
    private List<Map> user_maps = new ArrayList<>();
    private AuthenticatedUser authenticatedUser;

    private Toolbar toolbar;
    private Menu menu;
    private Button edit_btn;
    private Button delete_btn;
    private FloatingActionButton addMap_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initView();
        initData();
    }

    private void initView() {
        recyclerView = findViewById(R.id.user_recycler_view_map);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);

        menu.findItem(R.id.menu_item_login).setVisible(false);
        menu.findItem(R.id.menu_item_register).setVisible(false);
        menu.findItem(R.id.menu_item_create_new_map).setVisible(false);

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
            openProfileDetailsFragment(authenticatedUser.getUser());
        }
        if(id == R.id.menu_item_logout){
            onLogout();
        }

        return super.onOptionsItemSelected(item);
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
                }
            }
        });
    }

    @Override
    public void onMapItemClick(int position) {
        Map clickedMap = user_maps.get(position);

        Intent intent = new Intent(getApplicationContext(), ViewMapActivity.class);
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

    @Override
    public void onMapItemEditBtnClick(int position) {
        Map clickedMap = user_maps.get(position);

        Intent intent = new Intent(getApplicationContext(), EditMapActivity.class);
        intent.putExtra("map", clickedMap);
        startActivity(intent);
    }

    private void openProfileDetailsFragment(User user) {
        UserProfileDetailsFragment fragment = UserProfileDetailsFragment.newInstance(user);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container, fragment, TAG_DETAILS).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void onLogout(){
        viewModel.deleteAllAuthUsers();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}
