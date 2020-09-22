package finki.ukim.mk.map_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.facebook.stetho.Stetho;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.viewmodel.MainActivityViewModel;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener
        , RegisterFragment.OnFragmentInteractionListener{

    private final String TAG_LOGIN = "LOGIN_FRAGMENT";
    private final String TAG_REGISTER = "REGISTER_FRAGMENT";

    private AuthenticatedUser authenticatedUserApp;
    private MainActivityViewModel viewModel;

    private Button exploreButton;
    private Toolbar toolbar;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainActivityViewModel.class);

        initView();
        initListener();
        try {
            getAuthUser();
        } catch (ExecutionException e) {
            Toast.makeText(this, "ExecutionException: getAuthUser()", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (InterruptedException e) {
            Toast.makeText(this, "InterruptedException: getAuthUser()", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);

        menu.findItem(R.id.menu_item_create_new_map).setVisible(false);
        menu.findItem(R.id.menu_item_user_maps).setVisible(false);
        menu.findItem(R.id.menu_item_logout).setVisible(false);

        return true;
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//        Toast.makeText(this, "Menu Preparation", Toast.LENGTH_SHORT).show();
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.menu_item_create_new_map){
            Intent intent = new Intent(getApplicationContext(),CreateNewMapActivity.class);
            startActivity(intent);
        }
        if(id == R.id.menu_item_explore){
            Intent intent = new Intent(getApplicationContext(),ExploreMapActivity.class);

            if(authenticatedUserApp !=null){
                intent.putExtra("userAuthenticated", 1);
            }else {
                intent.putExtra("userAuthenticated", 0);
            }

            startActivity(intent);
        }
        if(id == R.id.menu_item_login){
            openLoginFragment();
        }
        if(id == R.id.menu_item_register){
            openRegisterFragment();
        }
        if(id == R.id.menu_item_profile){
//            Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
            Intent intent = new Intent(getApplicationContext(), UserProfileNewActivity.class);
            startActivity(intent);
        }

        if(id == R.id.menu_item_logout){
            onLogout();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openLoginFragment(){
        LoginFragment fragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container, fragment, TAG_LOGIN).commit();
    }

    public void openRegisterFragment(){
        RegisterFragment fragment = new RegisterFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container, fragment, TAG_REGISTER).commit();
    }

    private void initView()
    {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar.setTitle("MapApp");
        setSupportActionBar(toolbar);
        this.exploreButton = findViewById(R.id.ExploreButton);

    }

    private void initListener() {
        this.exploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExploreMapActivity.class);
                if(authenticatedUserApp != null){
                    intent.putExtra("userAuthenticated", 1);
                }else {
                    intent.putExtra("userAuthenticated", 0);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onFragmentInteraction(String TAG, AuthenticatedUser authenticatedUser) {
        switch (TAG){
            case TAG_LOGIN:
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                try {
                    getAuthUser();
                } catch (ExecutionException e) {
                    Toast.makeText(this, "ExecutionException: getAuthUser()", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    Toast.makeText(this, "InterruptedException: getAuthUser()", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
//                Toast.makeText(this, "Username:"+authenticatedUser.getUser().getUsername(), Toast.LENGTH_SHORT).show();
                changeMenu(R.id.menu_item_login,false);
                changeMenu(R.id.menu_item_register,false);
                changeMenu(R.id.menu_item_profile, true);
                changeMenu(R.id.menu_item_logout, true);
                onBackPressed();
                break;
            case TAG_REGISTER:
                Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show();
                try {
                    getAuthUser();
                } catch (ExecutionException e) {
                    Toast.makeText(this, "ExecutionException: getAuthUser()", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    Toast.makeText(this, "InterruptedException: getAuthUser()", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                changeMenu(R.id.menu_item_login,false);
                changeMenu(R.id.menu_item_register,false);
                changeMenu(R.id.menu_item_profile, true);
                changeMenu(R.id.menu_item_logout, true);

                onBackPressed();
                break;
        }
    }


    public void getAuthUser() throws ExecutionException, InterruptedException {
        AuthenticatedUser authUser = viewModel.getAuthUser();
        if(authUser!= null){
            authenticatedUserApp = authUser;
        }
    }

    public void changeMenu(int id, boolean visibility){
        if(menu == null){
            return;
        }

        MenuItem item = menu.findItem(id);
        item.setVisible(visibility);
    }

    private void onLogout(){
        changeMenu(R.id.menu_item_login,true);
        changeMenu(R.id.menu_item_register,true);
        changeMenu(R.id.menu_item_profile, false);
        changeMenu(R.id.menu_item_logout, false);

        viewModel.deleteAllAuthUsers();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}
