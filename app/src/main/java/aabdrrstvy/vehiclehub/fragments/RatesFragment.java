package aabdrrstvy.vehiclehub.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aabdrrstvy.vehiclehub.R;

public class RatesFragment extends Fragment {

    private OnRatesCalledListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnRatesCalledListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mybook, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListener.OnRatesCalled("");
    }

    public static Fragment newInstance() {
        return new RatesFragment();
    }

    public interface OnRatesCalledListener {
        void OnRatesCalled(String title);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
