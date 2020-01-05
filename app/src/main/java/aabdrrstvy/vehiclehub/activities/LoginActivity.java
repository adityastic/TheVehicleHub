package aabdrrstvy.vehiclehub.activities;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;

import aabdrrstvy.vehiclehub.R;
import aabdrrstvy.vehiclehub.fragments.splashscreen.LastFragment;
import aabdrrstvy.vehiclehub.fragments.splashscreen.LoginFragment;

public class LoginActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        final Slide loginSlide, lastSlide;

        addSlide(new SimpleSlide.Builder()
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .title("Hello")
                .build());

        loginSlide = new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .fragment(new LoginFragment().newInstance())
                .build();

        lastSlide = new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .fragment(new LastFragment().newInstance())
                .build();

        addSlide(loginSlide);

        addSlide(new SimpleSlide.Builder()
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .title("Lets Finalize Things")
                .build());

        addSlide(lastSlide);

        addSlide(new SimpleSlide.Builder()
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .title("Thank You")
                .description("Glad to have you on our team ;)")
                .build());
    }
}