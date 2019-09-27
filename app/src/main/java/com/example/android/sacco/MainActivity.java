package com.example.android.sacco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SessionHandler session;
    private String szxUserName;
    private String szxFullName;
    private Member mx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        Member m = session.getMemberDetails();
        mx = m;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView tvTitle = (TextView) header.findViewById(R.id.headertitle);
        TextView tvSubTitle = (TextView) header.findViewById(R.id.headersubtitle);

        szxUserName = user.getUsername();
        szxFullName = user.getFullName();

        tvTitle.setText(szxUserName);
        tvSubTitle.setText(szxFullName);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            session.logoutUser();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent iInt;

        switch (id)
        {
            case R.id.nav_profile:
                iInt = new Intent(getApplicationContext(), ProfileActivity.class);
                iInt.putExtra("UserName", szxUserName);
                iInt.putExtra("FullName", szxFullName);
                startActivity(iInt);
                break;

            case R.id.nav_aboutus:
               iInt = new Intent(getApplicationContext(), AboutUsActivity.class);
               startActivity(iInt);
               break;

            case R.id.nav_loanapplication:
                iInt = new Intent(getApplicationContext(), LoanApplicationActivity.class);
                startActivity(iInt);
                break;
            case R.id.nav_errornousdeductions:
                iInt = new Intent(getApplicationContext(), ErrenousDeductionActivity.class);
                startActivity(iInt);
                break;
            case R.id.nav_nextofkin:
                iInt = new Intent(getApplicationContext(), NextOfKinActivity.class);
                iInt.putExtra("MemberNo", mx.getMemberNo());
                startActivity(iInt);
                break;
            case R.id.nav_financials:
                iInt = new Intent(getApplicationContext(), FinancialsActivity.class);
                iInt.putExtra("MemberNo", mx.getMemberNo());
                startActivity(iInt);
                break;
        }
/*         else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
