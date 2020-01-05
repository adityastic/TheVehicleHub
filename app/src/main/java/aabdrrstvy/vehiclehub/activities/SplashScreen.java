package aabdrrstvy.vehiclehub.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import aabdrrstvy.vehiclehub.utils.Common;
import aabdrrstvy.vehiclehub.utils.firebase.FirebaseHub;

public class SplashScreen extends AppCompatActivity {

    public static final int REQUEST_CODE_INTRO = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SHARED PREFS
        Common.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("grid", true).apply();

        FirebaseHub.firebaseAuth = FirebaseAuth.getInstance();
        FirebaseHub.currentUser = FirebaseHub.firebaseAuth.getCurrentUser();

        if (!FirebaseHub.checkFirebaseUser()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_CODE_INTRO);
        } else {
            FirebaseHub.updateProfile();
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INTRO) {
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
            } else {
                //User cancelled the intro so we'll finish this activity too.
                finish();
            }
        }
    }
}
