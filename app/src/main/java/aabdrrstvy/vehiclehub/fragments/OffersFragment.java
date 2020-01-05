package aabdrrstvy.vehiclehub.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import aabdrrstvy.vehiclehub.R;
import aabdrrstvy.vehiclehub.adapter.OfferAdapter;
import aabdrrstvy.vehiclehub.views.HeaderView;

public class OffersFragment extends Fragment {

    OnOfferCalledListener mListener;

    RecyclerView mDealsRecyclerView, mOffersRecyclerView;
    RecyclerView.Adapter mOffersAdapter, mDealsAdapter;
    List<Bitmap> list;
    HeaderView headerView;

    public static Fragment newInstance() {
        return new OffersFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OffersFragment.OnOfferCalledListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListener.OnOfferCalled("3 Deals & Offers");

        headerView = view.findViewById(R.id.header_image);

//        Glide.with(this /* context */)
//                .using(new FirebaseImageLoader())
//                .load(storageReference)
//                .into(headerView);

        mDealsRecyclerView = view.findViewById(R.id.deals_recycler_view);
        mOffersRecyclerView = view.findViewById(R.id.offers_recycler_view);

        mDealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mOffersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        list = new ArrayList<>();
        list.add(BitmapFactory.decodeResource(getResources(), R.drawable.material_drawer_header));
        list.add(BitmapFactory.decodeResource(getResources(), R.drawable.material_drawer_header));
        list.add(BitmapFactory.decodeResource(getResources(), R.drawable.material_drawer_header));
        list.add(BitmapFactory.decodeResource(getResources(), R.drawable.material_drawer_header));

        mDealsAdapter = new OfferAdapter(getContext(), list);
        mOffersAdapter = new OfferAdapter(getContext(), list);

        mDealsRecyclerView.setAdapter(mDealsAdapter);
        mOffersRecyclerView.setAdapter(mOffersAdapter);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand-Bold.ttf");
        ((TextView) view.findViewById(R.id.offer_text)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.deals_text)).setTypeface(tf);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnOfferCalledListener {
        void OnOfferCalled(String subtitle);
    }

}
