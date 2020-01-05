package aabdrrstvy.vehiclehub.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Objects;

import aabdrrstvy.vehiclehub.R;
import aabdrrstvy.vehiclehub.adapter.MyBookingsAdapter;
import aabdrrstvy.vehiclehub.utils.firebase.FirebaseHub;

public class MyBookFragment extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    MyBookingsAdapter mAdapter;
    private OnMyBookCalledListener mListener;

    public static Fragment newInstance() {
        return new MyBookFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnMyBookCalledListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(Objects.requireNonNull(getActivity()).toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mybook, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        refreshLayout();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout();
            }
        });
    }

    public void refreshLayout() {
        mSwipeRefreshLayout.setRefreshing(true);
        mAdapter = null;
        mRecyclerView.setAdapter(null);
        FirebaseHub.getMyBookings(new FirebaseHub.onCompleteLoad() {
            @Override
            public void onComplete() {
                if (FirebaseHub.bookinglist != null) {
                    mListener.OnMyBookCalled(FirebaseHub.bookinglist.size() + " Booking");
                    mAdapter = new MyBookingsAdapter(getContext(), FirebaseHub.bookinglist);
                    mRecyclerView.setAdapter(mAdapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(getContext(), "Error in Fetching", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMyBookCalledListener {
        void OnMyBookCalled(String title);
    }

}
