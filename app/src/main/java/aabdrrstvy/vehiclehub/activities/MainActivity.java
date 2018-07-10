package aabdrrstvy.vehiclehub.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aabdrrstvy.vehiclehub.R;
import aabdrrstvy.vehiclehub.adapter.DrawerItemsAdapter;
import aabdrrstvy.vehiclehub.datas.DrawerItemData;
import aabdrrstvy.vehiclehub.fragments.HomeFragment;
import aabdrrstvy.vehiclehub.fragments.MyBookFragment;
import aabdrrstvy.vehiclehub.fragments.OffersFragment;
import aabdrrstvy.vehiclehub.fragments.RatesFragment;
import aabdrrstvy.vehiclehub.utils.Common;
import aabdrrstvy.vehiclehub.utils.firebase.FirebaseHub;
import aabdrrstvy.vehiclehub.views.Drawer.AdityaDrawerToggle;
import aabdrrstvy.vehiclehub.views.Drawer.AdityaNavigationLayout;

public class MainActivity extends AppCompatActivity implements OffersFragment.OnOfferCalledListener, MyBookFragment.OnMyBookCalledListener, RatesFragment.OnRatesCalledListener, HomeFragment.OnHomeCalledListener {

    Toolbar toolbar;
    List<DrawerItemData> list;
    AdityaNavigationLayout drawer;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;

    boolean backflag = false;

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen())
        {
            drawer.closeDrawer();
        }else
        {
            if(backflag){
                finishAffinity();
            }else {
                backflag = true;
                Toast.makeText(this, "Press Back Again to Exit App", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setActionBarTitle(String subtitle) {
        getSupportActionBar().setSubtitle(subtitle);
    }

    public void createDrawerItems() {
        mAdapter = null;

        list = new ArrayList<>();
        list.add(new DrawerItemData(Common.drawableToBitmap(getResources().getDrawable(R.drawable.nav_drawer_homepage)), "Book","Book", HomeFragment.newInstance()));
        list.add(new DrawerItemData(Common.drawableToBitmap(getResources().getDrawable(R.drawable.nav_drawer_offers)), "Offers","Offers", OffersFragment.newInstance()));
        list.add(new DrawerItemData(Common.drawableToBitmap(getResources().getDrawable(R.drawable.nav_drawer_mybookings)), "MyBookings","My Bookings", MyBookFragment.newInstance()));
        list.add(new DrawerItemData(Common.drawableToBitmap(getResources().getDrawable(R.drawable.nav_drawer_tips)), "Tips","Tips", MyBookFragment.newInstance()));

        mRecyclerView.setAdapter(null);
        mAdapter = new DrawerItemsAdapter(this, list , (AdityaNavigationLayout) findViewById(R.id.drawer), getSupportFragmentManager(),getSupportActionBar());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.list);
        drawer = (AdityaNavigationLayout) findViewById(R.id.drawer);

        ((TextView)findViewById(R.id.name)).setText(FirebaseHub.currentUser.getDisplayName());
        ((TextView)findViewById(R.id.email)).setText(FirebaseHub.currentUser.getEmail());

        //createDrawer();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_toolbar_navigation);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer();
                }
            });
        }

        AdityaDrawerToggle duoDrawerToggle = new AdityaDrawerToggle(this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        ((AdityaNavigationLayout) findViewById(R.id.drawer)).setDrawerListener(duoDrawerToggle);

        duoDrawerToggle.syncState();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        createDrawerItems();

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, HomeFragment.newInstance()).addToBackStack(null).commit();
        getSupportActionBar().setTitle("Book");

        //drawer.setSelection(1);
    }

    @Override
    public void OnRatesCalled(String subtitle) {
        setActionBarTitle(subtitle);
    }

    @Override
    public void onHomesCalled(String subtitle) {
        setActionBarTitle(subtitle);
    }

    @Override
    public void OnMyBookCalled(String title) {
        setActionBarTitle(title);
    }

    @Override
    public void OnOfferCalled(String title) {
        setActionBarTitle(title);
    }
}
