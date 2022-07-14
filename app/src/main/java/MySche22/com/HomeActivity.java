package MySche22.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import MySche22.com.Database.Database;
import MySche22.com.Fragment.JointScheduleFragment;
import MySche22.com.Fragment.LogoutFragment;
import MySche22.com.Fragment.PersonalScheduleFragment;
import MySche22.com.Fragment.ProfileFragment;
import MySche22.com.Fragment.SettingFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private static final int FRAGMENT_PROFLIE=0;
    private static final int FRAGMENT_SETTING=1;
    private static final int FRAGMENT_LOGOUT=2;
    private static final int FRAGMENT_PERSONALSCHEDULE=3;
    private static final int FRAGMENT_JOINTSCHEDULE=4;

    private static final  int TIME_INTERVAL = 2000;
    private long backPressed;

    private int currentFragment = FRAGMENT_PERSONALSCHEDULE;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    HomeActivity homeActivity;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navInfo();

        sharedPreferences=getSharedPreferences("Data", MODE_PRIVATE);
        editor=sharedPreferences.edit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_personal_schedule:
                        openPersonalScheduleFragment();
                        NavigationView navigationView = findViewById(R.id.navigationView);
                        navigationView.setCheckedItem(R.id.nav_personal_schedule);
                        setTitleToolbar();
                        break;
                    case R.id.nav_joint_schedule:
                        openJointScheduleFragment();
                        NavigationView navigationView1 = findViewById(R.id.navigationView);
                        navigationView1.setCheckedItem(R.id.nav_joint_schedule);
                        setTitleToolbar();

                        break;
                }
                return true;
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new PersonalScheduleFragment());
        navigationView.getMenu().findItem(R.id.nav_personal_schedule).setChecked(true);
        bottomNavigationView.getMenu().findItem(R.id.nav_personal_schedule).setChecked(true);
        setTitleToolbar();
    }

        public void LogoutConfirm() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage(R.string.logout_message);
        builder.setNegativeButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences prefs = getSharedPreferences("AccountPrefs", MODE_PRIVATE);
                prefs.edit().clear().apply();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.show();
     }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile){
            openProfileFragment();
        } else if (id == R.id.nav_setting){
            openSettingFragment();
        }  else if (id == R.id.nav_logout){
                LogoutConfirm();
        }else if (id == R.id.nav_personal_schedule) {
            openPersonalScheduleFragment();
            bottomNavigationView.getMenu().findItem(R.id.nav_personal_schedule).setChecked(true);
        } else if (id == R.id.nav_joint_schedule) {
        openJointScheduleFragment();
            bottomNavigationView.getMenu().findItem(R.id.nav_joint_schedule).setChecked(true);
        }
        setTitleToolbar();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
        }


    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if(backPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        } else{
            Toast.makeText(getBaseContext(),"Nhấn lần nữa để thoát",
                    Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }


    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFrame,fragment);
        transaction.commit();
    }

    private void setTitleToolbar(){
        String title = "";
        switch (currentFragment){
            case FRAGMENT_PERSONALSCHEDULE:
                title = getString(R.string.nav_personal_scheduld);
                break;
            case FRAGMENT_SETTING:
                title = getString(R.string.nav_setting);

                break;
            case FRAGMENT_PROFLIE:
                title = getString(R.string.nav_profile);

                break;
            case FRAGMENT_JOINTSCHEDULE:
                title = getString(R.string.nav_jointschedule);

                break;

        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
    }

    private void openPersonalScheduleFragment(){
        if (currentFragment != FRAGMENT_PERSONALSCHEDULE){
            replaceFragment(new PersonalScheduleFragment());
            currentFragment = FRAGMENT_PERSONALSCHEDULE;
        }
    }

    private void openSettingFragment(){
        if (currentFragment != FRAGMENT_SETTING){
            replaceFragment(new SettingFragment());
            currentFragment = FRAGMENT_SETTING;
        }
    }
    private void openProfileFragment(){
        if (currentFragment != FRAGMENT_PROFLIE){
            replaceFragment(new ProfileFragment());
            currentFragment = FRAGMENT_PROFLIE;
        }
    }
    private void openJointScheduleFragment(){
        if (currentFragment != FRAGMENT_JOINTSCHEDULE){
            replaceFragment(new JointScheduleFragment());
            currentFragment = FRAGMENT_JOINTSCHEDULE;
        }
    }

    public void navInfo(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        TextView userNameNAV = headerView.findViewById(R.id.userNameNAV);
        TextView userMailNAV = headerView.findViewById(R.id.userEmailNAV);
        String account = getIntent().getStringExtra("account");
        Database database = new Database(this);
        Cursor cursorNAV = database.getInfoNAV(account);

        while (cursorNAV.moveToNext()) {
            userNameNAV.setText(cursorNAV.getString(4) + " " + (cursorNAV.getString(3)));
            userMailNAV.setText(cursorNAV.getString(6));

        }

    }





}