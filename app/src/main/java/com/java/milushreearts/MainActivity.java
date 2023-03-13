package com.java.milushreearts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public NavigationView nav;
    public ActionBarDrawerToggle toggle;
    public DrawerLayout drawerLayout;
    public Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrameLayout, new HomeFragment()).commit();
        //nav.setCheckedItem(R.id.menu_home);

        // Setting of new Action Bar
       // Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.milushree_logopng);


        // Toggle initalization
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Fragment temp;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.menu_home :
                        temp = new HomeFragment();
                        break;
                    case R.id.menu_homegrid:
                        temp = new GridFragment();
                        break;
                    case R.id.menu_profile :
                        temp = new ProfileFragment();
                        break;
                    case R.id.menu_cart :
                        temp = new CartFragment();
                        break;
                    case R.id.menu_setting :
                        temp = new SettingFragment();
                        break;
                    case R.id.menu_logout :
                        temp = new LogoutFragment();
                        break;
                    case  R.id.menu_upload :
                        temp = new UploadFragment();
                        break;


                }
                getSupportFragmentManager().beginTransaction().replace(R.id.containerFrameLayout, temp).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;


            }
        });

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_icon, menu );

        return true;
    }*/




    private void initView() {
        nav = findViewById(R.id.nav_menu);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
    }
}