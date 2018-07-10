package aabdrrstvy.vehiclehub.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import aabdrrstvy.vehiclehub.R;
import aabdrrstvy.vehiclehub.adapter.FleetAdapter;
import aabdrrstvy.vehiclehub.datas.FleetInfo;
import aabdrrstvy.vehiclehub.utils.datePicker.SublimePickerFragment;
import aabdrrstvy.vehiclehub.utils.firebase.FirebaseHub;
import aabdrrstvy.vehiclehub.utils.interpolator.CustomBounceInterpolator;

public class SelectedFleetActivity extends AppCompatActivity {

    Toolbar toolbar;
    CardView cardView;
    ImageView curveRect;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public static void setViewAnimation(Context context,
                                        View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_bottom);
        view.startAnimation(animation);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_fleet);

        toolbar = findViewById(R.id.toolbar);
        cardView = findViewById(R.id.paytm);
        curveRect = findViewById(R.id.curved_rectImage);

        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);

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

        curveRect.setAnimation(myAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setViewAnimation(SelectedFleetActivity.this,cardView);
            }
        },400);

        //Animations
        Fade slide = new Fade();
        slide.setDuration(1000);
        getWindow().setEnterTransition(slide);

    }
}
