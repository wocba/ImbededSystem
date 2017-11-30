package com.wocba.imbededsystem.Common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.wocba.imbededsystem.Camera.CameraActivity;
import com.wocba.imbededsystem.Content.ContentActivity;
import com.wocba.imbededsystem.Main.MainActivity;

import com.wocba.imbededsystem.R;

/**
 * Created by jinwo on 2017-09-06.
 */


public class BaseActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener{


    private Toolbar mActionBarToolbar;  // 툴바
    private DrawerLayout mDrawerLayout;
    protected NavigationView mNavigationView;
    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth mAuth;

    protected  boolean useToolbar(){
        return true;
    }
    protected boolean useDrawerToggle(){
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    protected Toolbar getActionBarToolbar(){
        if(mActionBarToolbar == null){
            mActionBarToolbar = (Toolbar)findViewById(R.id.toolbar);
            if(mActionBarToolbar != null){
                mActionBarToolbar.setNavigationContentDescription(getResources()
                        .getString(R.string.navdrawer_description_a11y));
                if(useToolbar()){
                    setSupportActionBar(mActionBarToolbar);
                }else {
                    mActionBarToolbar.setVisibility(View.GONE);
                }
            }
        }
        return mActionBarToolbar;
    }
    private void setupNavDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            return;
        }
        // use the hamburger menu
        if( useDrawerToggle()) {
            mToggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, mActionBarToolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            mDrawerLayout.setDrawerListener(mToggle);
            mToggle.syncState();
        }
        else if(useToolbar() && getSupportActionBar() != null) {
            // Use home/back button instead
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat
                    .getDrawable(this, R.drawable.abc_ic_ab_back_material));
        }

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        getActionBarToolbar();
        setupNavDrawer();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void createBackStack(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder builder = TaskStackBuilder.create(this);
            builder.addNextIntentWithParentStack(intent);
            builder.startActivities();
        } else {
            startActivity(intent);
            finish();
        }
    }



    public boolean onNavigationItemSelected(MenuItem item){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int id = item.getItemId();
        switch (id){
            case R.id.nav_home:
                createBackStack(new Intent(this , MainActivity.class));
                break;
            case R.id.nav_content:
                createBackStack(new Intent(this, ContentActivity.class));
                break;
            case R.id.nav_camera:
                Intent intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        closeNavDrawer();
        return true;
    }

}
