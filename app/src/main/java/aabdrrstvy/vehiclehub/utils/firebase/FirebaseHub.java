package aabdrrstvy.vehiclehub.utils.firebase;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import aabdrrstvy.vehiclehub.datas.Booking;
import aabdrrstvy.vehiclehub.datas.FleetInfo;
import aabdrrstvy.vehiclehub.datas.Profile;
import aabdrrstvy.vehiclehub.utils.Common;

public class FirebaseHub {
    public static FirebaseAuth firebaseAuth;
    public static FirebaseUser currentUser;

    private static DatabaseReference profileDataBase;
    private static Profile profile;

    public static boolean checkFirebaseUser() {
        return currentUser != null;
    }

    public static void updateProfile() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        profileDataBase = database.getReference("Profiles");

        profile = new Profile(FirebaseHub.currentUser.getDisplayName(),
                FirebaseHub.currentUser.getEmail(),
                FirebaseHub.currentUser.getPhoneNumber());

        profileDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(FirebaseHub.currentUser.getUid())) {
                    profileDataBase.child(FirebaseHub.currentUser.getUid()).setValue(profile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static ArrayList<FleetInfo> fleetlist;
    private static int count = 0;

    public static void getFleetDetails(final Calendar start, final Calendar end, final onCompleteLoad onCompleteLoad) {
        fleetlist = new ArrayList<>();
        count = 0;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("VehicleInfo");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            count++;
                            Map fleetinfo = (Map) data.getValue();
                            StringTokenizer str = new StringTokenizer(fleetinfo != null ? fleetinfo.get("Rate").toString() : null, "|");
                            int rate = Integer.parseInt(str.nextToken());
                            int rate_ex = Integer.parseInt(str.nextToken());
                            int rate_late = Integer.parseInt(str.nextToken());

                            fleetlist.add(new FleetInfo(
                                    fleetinfo != null ? fleetinfo.get("BrandModel").toString() : null,
                                    fleetinfo != null ? fleetinfo.get("VUrl").toString() : null,
                                    data.getKey(),
                                    rate,
                                    rate_ex,
                                    rate_late
                            ));
                        }

                        if (count == dataSnapshot.getChildrenCount()) {
                            count = 0;
                            DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference().child("Booking");
                            bookingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @SuppressLint("SimpleDateFormat")
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        count++;
                                        for (DataSnapshot dateChild : data.getChildren()) {
                                            Map booking = (Map) dateChild.getValue();
                                            Date startDate = null, endDate = null;
                                            try {
                                                startDate = new SimpleDateFormat("dd MM yyyy hh:mm a").parse(booking != null ? booking.get("startDate").toString() : null);
                                                endDate = new SimpleDateFormat("dd MM yyyy hh:mm a").parse(booking != null ? booking.get("endDate").toString() : null);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            Calendar newDate = Calendar.getInstance();
                                            newDate.setTime(new Date((endDate != null ? endDate.getTime() : 0) + TimeUnit.HOURS.toMillis(2)));

                                            if (Common.checkGreaterDate(startDate, newDate.getTime(), start.getTime()) || Common.checkGreaterDate(startDate, newDate.getTime(), end.getTime())) {
                                                if (Common.getListIndex(fleetlist, booking != null ? booking.get("number").toString() : null) != -1) {
                                                    fleetlist.remove(Common.getListIndex(fleetlist, booking != null ? booking.get("number").toString() : null));
                                                }
                                            }
                                        }
                                    }

                                    if (count == dataSnapshot.getChildrenCount()) {
                                        onCompleteLoad.onComplete();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    fleetlist = null;
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        fleetlist = null;
                    }
                });
    }

    @SuppressLint("SimpleDateFormat")
    public static void addBooking(Calendar startJourney, Calendar endJourney, Double amount, String number) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bookingDatabase = database.getReference("Booking");
        profileDataBase = database.getReference("Profiles");

        @SuppressLint("SimpleDateFormat") Booking booking = new Booking(
                new SimpleDateFormat("dd MM yyyy hh:mm a").format(startJourney.getTime()),
                new SimpleDateFormat("dd MM yyyy hh:mm a").format(endJourney.getTime()),
                amount,
                currentUser.getUid(),
                number);
        String key = bookingDatabase.push().getKey();

        bookingDatabase.child(new SimpleDateFormat("dd-MM-yyyy").format(startJourney.getTime())).child(key).setValue(booking);
        profileDataBase.child(FirebaseHub.currentUser.getUid()).child("MyBookings").child(profileDataBase.push().getKey()).setValue(new SimpleDateFormat("dd-MM-yyyy").format(startJourney.getTime()) + "|" + key + "|" + number);
    }

    public static ArrayList<FleetInfo> bookinglist;
    private static ArrayList<String> numbers;

    public static void getMyBookings(final onCompleteLoad onCompleteLoad) {
        bookinglist = new ArrayList<>();
        numbers = new ArrayList<>();
        count = 0;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Profiles").child(currentUser.getUid()).child("MyBookings");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            count++;

                            StringTokenizer str = new StringTokenizer(Objects.requireNonNull(data.getValue()).toString(), "|");
                            String date = str.nextToken();
                            String bookid = str.nextToken();
                            numbers.add(str.nextToken());
                        }

                        if (count == dataSnapshot.getChildrenCount()) {
                            count = 0;
                            DatabaseReference vehicleRef = FirebaseDatabase.getInstance().getReference().child("VehicleInfo");
                            vehicleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        count++;
                                        Map fleetinfo = (Map) data.getValue();

                                        int numbersBooked = timesBooked(data.getKey());

                                        if (numbersBooked != -1) {
                                            StringTokenizer str = new StringTokenizer(fleetinfo != null ? fleetinfo.get("Rate").toString() : null, "|");
                                            int rate = Integer.parseInt(str.nextToken());
                                            int rate_ex = Integer.parseInt(str.nextToken());
                                            int rate_late = Integer.parseInt(str.nextToken());

                                            for(int i=0;i<numbersBooked;i++)
                                            {
                                                bookinglist.add(new FleetInfo(
                                                        fleetinfo != null ? fleetinfo.get("BrandModel").toString() : null,
                                                        fleetinfo != null ? fleetinfo.get("VUrl").toString() : null,
                                                        dataSnapshot.getKey(),
                                                        rate,
                                                        rate_ex,
                                                        rate_late
                                                ));
                                            }
                                        }
                                    }

                                    if (count == dataSnapshot.getChildrenCount()) {
                                        onCompleteLoad.onComplete();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    bookinglist = null;
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        bookinglist = null;
                    }
                });
    }

    private static int timesBooked(String key) {
        int count = 0;

        for (int j = 0; j < numbers.size(); j++) {
            if (numbers.get(j).equals(key)) {
                count++;
            }
        }
        if (count >= 0)
            return count;
        else
            return -1;
    }

    public interface onCompleteLoad {
        void onComplete();
    }
}

