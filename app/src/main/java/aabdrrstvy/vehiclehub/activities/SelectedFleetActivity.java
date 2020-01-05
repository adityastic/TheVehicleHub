package aabdrrstvy.vehiclehub.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import aabdrrstvy.vehiclehub.R;
import aabdrrstvy.vehiclehub.utils.interpolator.CustomBounceInterpolator;

public class SelectedFleetActivity extends AppCompatActivity {

    Toolbar toolbar;
    CardView cardView;
    ImageView curveRect;

    public static void setViewAnimation(Context context,
                                        View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_bottom);
        view.startAnimation(animation);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
                setViewAnimation(SelectedFleetActivity.this, cardView);
            }
        }, 400);

        //Animations
        Fade slide = new Fade();
        slide.setDuration(1000);
        getWindow().setEnterTransition(slide);

    }
}
