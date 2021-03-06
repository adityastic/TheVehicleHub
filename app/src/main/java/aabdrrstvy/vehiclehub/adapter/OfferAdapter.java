package aabdrrstvy.vehiclehub.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import aabdrrstvy.vehiclehub.R;

/**
 * Created by adityagupta on 10/12/17.
 */

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    Context context;
    List<Bitmap> list;

    public OfferAdapter(Context context, List<Bitmap> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_offer_item, parent, false);
        return new OfferViewHolder(v);
    }

//
//    public static void setRecyclerViewAnimation(Context context,
//                                                View view,
//                                                int animation_resource) {
//        Animation animation = AnimationUtils.loadAnimation(context, animation_resource);
//        view.startAnimation(animation);
//    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        Bitmap data = list.get(position);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView offer;

        public OfferViewHolder(View itemView) {
            super(itemView);
            offer = itemView.findViewById(R.id.offer_image);
        }

    }
}
