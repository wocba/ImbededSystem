package com.wocba.imbededsystem.Option;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.support.design.widget.NavigationView;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.wocba.imbededsystem.Content.ContentActivity;
import com.wocba.imbededsystem.Main.MainActivity;
import com.wocba.imbededsystem.Nfc.NfcActivity;
import com.wocba.imbededsystem.R;

/**
 * Created by jinwo on 2017-12-04.
 */

public class OptionActivity extends PreferenceActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar mActionBarToolbar;
    private DrawerLayout mDrawerLayout;
    protected NavigationView mNavigationView;
    private ActionBarDrawerToggle mToggle;
    private ActionBar supportActionBar;

    protected boolean useToolbar() {
        return true;
    }

    protected boolean useDrawerToggle() {
        return true;
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        getActionBarToolbar();

        setupNavDrawer();
    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mActionBarToolbar != null) {
                // Depending on which version of Android you are on the Toolbar or the ActionBar may be
                // active so the a11y description is set here.
                mActionBarToolbar.setNavigationContentDescription(getResources()
                        .getString(R.string.navdrawer_description_a11y));
                setSupportActionBar(mActionBarToolbar);

                if (useToolbar()) {
                    setSupportActionBar(mActionBarToolbar);
                } else { mActionBarToolbar.setVisibility(View.GONE); }

            }
        }

        return mActionBarToolbar;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.option_main);
        setContentView(R.layout.activity_option);
        mNavigationView.getMenu().getItem(2).setChecked(true);
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);

        return true;

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if(preference.getKey().equals("pref_push"))
        {
            CheckBoxPreference pref = (CheckBoxPreference) preference;
            if(pref.isChecked())
            {
                savePushOnPreferences();
            }
            else
            {
                savePushOffPreferences();
            }
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
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
            case R.id.nav_option:
                createBackStack(new Intent(this, OptionActivity.class));
                break;
            case R.id.nav_nfc:
                createBackStack(new Intent(this, NfcActivity.class));
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void createBackStack(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder builder = TaskStackBuilder.create(this);
            builder.addNextIntentWithParentStack(intent);
            builder.startActivities();
        } else {
            startActivity(intent);
            finish();
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


    private void setupNavDrawer() {


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            return;
        }


        // use the hamburger menu
        if (useDrawerToggle()) {
            mToggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, mActionBarToolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            mDrawerLayout.setDrawerListener(mToggle);
            mToggle.syncState();
        } else if (useToolbar() && getSupportActionBar() != null) {
            // Use home/back button instead
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat
                    .getDrawable(this, R.drawable.abc_ic_ab_back_material));
        }

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

    }

    public void setSupportActionBar(Toolbar supportActionBar) {
        supportActionBar = supportActionBar;
    }

    public ActionBar getSupportActionBar() {
        return supportActionBar;
    }

    private void savePushOnPreferences()
    {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("push", true);
        editor.commit();
    }

    private void savePushOffPreferences()
    {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("push", false);
        editor.commit();
    }

}
