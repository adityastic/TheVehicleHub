package aabdrrstvy.vehiclehub.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import aabdrrstvy.vehiclehub.R;
import aabdrrstvy.vehiclehub.activities.FleetActivity;
import aabdrrstvy.vehiclehub.adapter.HintAdapter;
import aabdrrstvy.vehiclehub.utils.datePicker.SublimePickerFragment;

public class HomeFragment extends Fragment {

    OnHomeCalledListener mListener;

    Animation animation;
    AppCompatImageView imageView;
    AppCompatSpinner spinner;
    SwipeRefreshLayout swipeRefreshLayout;
    CoordinatorLayout searchLayout;

    LinearLayout startDateLayout, endDateLayout;

    //Starting Date Layouts
    AppCompatTextView startDate, startMonthYear, startTime;
    AppCompatTextView endDate, endMonthYear, endTime;

    Calendar startJourney, endJourney;
    String[] monthNames = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    String[] hourNames = new String[]{"12", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"};

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    public void setStartDateTime(Calendar cal) {
        startJourney = cal;
        startDate.setText(cal.get(Calendar.DATE) + "");
        startMonthYear.setText((monthNames[cal.get(Calendar.MONTH)] + "-" + cal.get(Calendar.YEAR)) + "");
        startTime.setText((hourNames[cal.get(Calendar.HOUR)] + ":" + String.format("%02d", cal.get(Calendar.MINUTE))) + " " + ((cal.get(Calendar.AM_PM) == Calendar.PM) ? "PM" : "AM") + "");
    }

    public void setEndDateTime(Calendar cal) {
        endJourney = cal;
        endDate.setText(cal.get(Calendar.DATE) + "");
        endMonthYear.setText((monthNames[cal.get(Calendar.MONTH)] + "-" + cal.get(Calendar.YEAR)) + "");
        endTime.setText((hourNames[cal.get(Calendar.HOUR)] + ":" + String.format("%02d", cal.get(Calendar.MINUTE))) + " " + ((cal.get(Calendar.AM_PM) == Calendar.PM) ? "PM" : "AM") + "");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (HomeFragment.OnHomeCalledListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void setSearchLayout() {
        if (Math.random() > 0.5) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.bg_left));
            animation = AnimationUtils.loadAnimation(getContext(), R.anim.search_ltr);
        } else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.bg_right));
            animation = AnimationUtils.loadAnimation(getContext(), R.anim.search_rtl);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.startAnimation(animation);
                imageView.setVisibility(View.VISIBLE);

                searchLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_from_bottom));
                searchLayout.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    public void setSpinner() {
        ArrayList<String> s = new ArrayList<>();
        s.add("Select Pick Up");
        s.add("Front Gate, Bharati Vidyapeeth");
        HintAdapter adapter = new HintAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, s);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(adapter);
    }

    public void refreshLayout() {
        imageView.setVisibility(View.GONE);
        searchLayout.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setSearchLayout();
                setSpinner();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 500);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListener.onHomesCalled("Get 1 hr free");

        imageView = view.findViewById(R.id.search_bg);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        searchLayout = view.findViewById(R.id.searchCoordinator);
        spinner = view.findViewById(R.id.spinner);
        startDateLayout = view.findViewById(R.id.startDatePickerLayout);
        endDateLayout = view.findViewById(R.id.endDatePickerLayout);
        startDate = view.findViewById(R.id.startDate);
        startMonthYear = view.findViewById(R.id.startmonthyear);
        startTime = view.findViewById(R.id.starttime);
        endDate = view.findViewById(R.id.endDate);
        endMonthYear = view.findViewById(R.id.endmonthyear);
        endTime = view.findViewById(R.id.endtime);
        AppCompatButton button = view.findViewById(R.id.search);

        swipeRefreshLayout.setRefreshing(true);

        refreshLayout();

        //LayoutFirstChanges
        Calendar temp = Calendar.getInstance();
        setStartDateTime(temp);
        temp = Calendar.getInstance();
        temp.setTime(new Date(temp.getTime().getTime() + TimeUnit.HOURS.toMillis(3)));
        setEndDateTime(temp);

        //Listeners
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

                    }
                });
                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getFragmentManager(), "SUBLIME_PICKER");
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
                        } else {
                            Toast.makeText(getActivity(), "Select Dates Prior to 3 hours from the Starting time", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getFragmentManager(), "SUBLIME_PICKER");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItemPosition() > 0) {
                    Intent i = new Intent(getContext(), FleetActivity.class);
                    i.putExtra("startCal", startJourney.getTimeInMillis());
                    i.putExtra("endCal", endJourney.getTimeInMillis());
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
                } else
                    Toast.makeText(getContext(), "Select Pickup", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnHomeCalledListener {
        void onHomesCalled(String subtitle);
    }

}
