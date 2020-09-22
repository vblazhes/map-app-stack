package finki.ukim.mk.map_application;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import finki.ukim.mk.map_application.adapter.MapRecyclerViewAdapter;
import finki.ukim.mk.map_application.adapter.listener.onMapItemClickListener;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.viewmodel.ExploreMapViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExploreMapActivity extends AppCompatActivity implements onMapItemClickListener {

    private ExploreMapViewModel exploreMapViewModel;
    private RecyclerView recyclerView;
    private MapRecyclerViewAdapter adapter;
    private List<Map> maps_activity = new ArrayList<>();
    private Toolbar toolbar;
    private int userAuthenticated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_map);

        /** Create a ViewModel the first time the system calls an activity's onCreate() method.
         Re-created activities receive the same MyViewModel instance created by the first activity.**/

        Intent intent = getIntent();
        userAuthenticated = intent.getIntExtra("userAuthenticated",2);

        initView();
        initData();
    }

    private void initView(){

        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar.setTitle("MapApp");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view_map);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new MapRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

    }

    private void initData() {
        System.out.println("Before VM Creation");
        exploreMapViewModel =  ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(ExploreMapViewModel.class);

        System.out.println("After VM Creation:"+ exploreMapViewModel!=null? "Created":"Not Created");


        exploreMapViewModel.getAllApprovedMaps().observe(this, new Observer<List<Map>>() {
            @Override
            public void onChanged(List<Map> maps) {
                adapter.setMaps(maps);
                if(maps.size() > 0)
                {
                    maps_activity.clear();
                    maps_activity.addAll(maps);
                }
            }
        });
    }

    @Override
    public void onMapItemClick(int position) {
        //Navigate to new activity

        Map clickedMap = maps_activity.get(position);

        Intent intent = new Intent(getApplicationContext(), ViewMapActivity.class);
        intent.putExtra("map", clickedMap);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);

        menu.findItem(R.id.menu_dots).setVisible(false);
        menu.findItem(R.id.menu_item_create_new_map).setVisible(false);
        menu.findItem(R.id.menu_item_explore).setVisible(false);
        menu.findItem(R.id.menu_item_user_maps).setVisible(false);
        menu.findItem(R.id.menu_item_login).setVisible(false);
        menu.findItem(R.id.menu_item_register).setVisible(false);
        menu.findItem(R.id.menu_item_logout).setVisible(false);
        menu.findItem(R.id.menu_item_profile).setVisible(false);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        searchItem.setVisible(true);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
}
