package aabdrrstvy.vehiclehub.activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import aabdrrstvy.vehiclehub.R;
import aabdrrstvy.vehiclehub.adapter.FleetAdapter;
import aabdrrstvy.vehiclehub.datas.FleetInfo;
import aabdrrstvy.vehiclehub.utils.Common;
import aabdrrstvy.vehiclehub.utils.datePicker.SublimePickerFragment;
import aabdrrstvy.vehiclehub.utils.firebase.FirebaseHub;
import aabdrrstvy.vehiclehub.utils.interpolator.CustomBounceInterpolator;

public class FleetActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    FleetAdapter mAdapter;
    Toolbar toolbar;
    LinearLayout startDateLayout, endDateLayout;
    // Date Layouts
    AppCompatTextView startDate, startTime;
    AppCompatTextView endDate, endTime;

    Calendar startJourney, endJourney;
    String[] monthNames = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    String[] hourNames = new String[]{"12", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"};


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fleet, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_toolbar:
                refreshLayout();
                break;
        }
        return true;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void setStartDateTime(Calendar cal) {
        startJourney = cal;
        if (mAdapter != null)
            mAdapter.setStartJourney(startJourney);
        Log.e("StartTime", cal.getTime().toString());
        startDate.setText((cal.get(Calendar.DATE) + "-" + monthNames[cal.get(Calendar.MONTH)] + "-" + cal.get(Calendar.YEAR)) + "");
        startTime.setText((hourNames[cal.get(Calendar.HOUR)] + ":" + String.format("%02d", cal.get(Calendar.MINUTE))) + " " + ((cal.get(Calendar.AM_PM) == Calendar.PM) ? "PM" : "AM") + "");
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void setEndDateTime(Calendar cal) {
        endJourney = cal;
        if (mAdapter != null)
            mAdapter.setEndJourney(endJourney);
        Log.e("EndTime", cal.getTime().toString());
        endDate.setText((cal.get(Calendar.DATE) + "-" + monthNames[cal.get(Calendar.MONTH)] + "-" + cal.get(Calendar.YEAR)) + "");
        endTime.setText((hourNames[cal.get(Calendar.HOUR)] + ":" + String.format("%02d", cal.get(Calendar.MINUTE))) + " " + ((cal.get(Calendar.AM_PM) == Calendar.PM) ? "PM" : "AM") + "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet);

        sharedPreferences = Common.getDefaultSharedPreferences(getApplicationContext());

        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh);
        mRecyclerView = findViewById(R.id.recycler_view);
        toolbar = findViewById(R.id.toolbar);
        startDate = findViewById(R.id.startDate);
        startTime = findViewById(R.id.starttime);
        endDate = findViewById(R.id.endDate);
        endTime = findViewById(R.id.endtime);
        startDateLayout = findViewById(R.id.startDatePickerLayout);
        endDateLayout = findViewById(R.id.endDatePickerLayout);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        CustomBounceInterpolator interpolator = new CustomBounceInterpolator(0.3, 15);
        myAnim.setInterpolator(interpolator);
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout();
                    }
                },200);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Calendar start, end;
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.setTimeInMillis(Objects.requireNonNull(getIntent().getExtras()).getLong("startCal"));
        end.setTimeInMillis(getIntent().getExtras().getLong("endCal"));
        setStartDateTime(start);
        setEndDateTime(end);


        //Animations
        Fade slide = new Fade();
        slide.setDuration(1000);
        getWindow().setEnterTransition(slide);

        if (sharedPreferences.getBoolean("grid", true)) {
            ((ImageView) findViewById(R.id.gridlinearbutton)).setImageDrawable(getResources().getDrawable(R.drawable.ic_selectlayout_linear));
        } else {
            ((ImageView) findViewById(R.id.gridlinearbutton)).setImageDrawable(getResources().getDrawable(R.drawable.ic_selectlayout_grid));
        }
        mRecyclerView.setLayoutManager((sharedPreferences.getBoolean("grid", true)) ? new GridLayoutManager(this, 2) : new LinearLayoutManager(this));

        (findViewById(R.id.gridlinearbutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getBoolean("grid", true)) {
                    sharedPreferences.edit().putBoolean("grid", false).apply();
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(FleetActivity.this));
                    ((ImageView) findViewById(R.id.gridlinearbutton)).setImageDrawable(getResources().getDrawable(R.drawable.ic_selectlayout_grid));
                } else {
                    sharedPreferences.edit().putBoolean("grid", true).apply();
                    mRecyclerView.setLayoutManager(new GridLayoutManager(FleetActivity.this, 2));
                    ((ImageView) findViewById(R.id.gridlinearbutton)).setImageDrawable(getResources().getDrawable(R.drawable.ic_selectlayout_linear));
                }
            }
        });

        (findViewById(R.id.curved_rectImage)).startAnimation(myAnim);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout();
            }
        });
        startDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(new SublimePickerFragment.Callback() {
                    @Override
                    public void onCancelled() {

                    }

                    @Override
                    public void onDateTimeRecurrenceSet(SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
                        Calendar date = selectedDate.getFirstDate();
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        setStartDateTime(date);
                        Calendar newDate = Calendar.getInstance();
                        newDate.setTime(new Date(date.getTime().getTime() + TimeUnit.HOURS.toMillis(3)));
                        setEndDateTime(newDate);
                        refreshLayout();
                    }
                });
                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
            }
        });

        endDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(new SublimePickerFragment.Callback() {
                    @Override
                    public void onCancelled() {

                    }

                    @Override
                    public void onDateTimeRecurrenceSet(SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
                        Calendar date = selectedDate.getFirstDate();
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);

                        if ((date.getTime().getTime() - startJourney.getTime().getTime()) >= TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS)) {
                            setEndDateTime(date);
                            refreshLayout();
                        } else {
                            Toast.makeText(FleetActivity.this, "Select Dates Prior to 3 hours from the Starting time", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
            }
        });
    }

    public void refreshLayout() {
        mSwipeRefreshLayout.setRefreshing(true);
        mAdapter = null;
        mRecyclerView.setAdapter(null);

        FirebaseHub.getFleetDetails(startJourney, endJourney, new FirebaseHub.onCompleteLoad() {
            @Override
            public void onComplete() {
                if (FirebaseHub.fleetlist != null) {
                    mAdapter = new FleetAdapter(FleetActivity.this, FirebaseHub.fleetlist, startJourney, endJourney, FleetActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(FleetActivity.this, "Error in Fetching", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
}
