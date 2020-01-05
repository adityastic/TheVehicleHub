package aabdrrstvy.vehiclehub.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import aabdrrstvy.vehiclehub.R;
import aabdrrstvy.vehiclehub.datas.FleetInfo;

/**
 * Created by adityagupta on 10/12/17.
 */

public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.FleetViewHolder> {

    private static int lastPosition = -1;
    private Context context;
    private List<FleetInfo> list;

    public MyBookingsAdapter(Context context, List<FleetInfo> list) {
        this.list = list;
        this.context = context;
    }

    private static void setRecyclerViewAnimation(Context context,
                                                 View view, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.slide_from_bottom
                : R.anim.slide_from_top);
        view.startAnimation(animation);
        lastPosition = position;
    }

    private static void setViewAnimation(Context context,
                                         View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.right_from_left);
        view.startAnimation(animation);
    }

    @NonNull
    @Override
    public FleetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_book_item, parent, false);
        return new FleetViewHolder(v);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FleetViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(@NonNull final FleetViewHolder holder, int position) {
        final FleetInfo data = list.get(position);

        holder.fleet_image.setVisibility(View.INVISIBLE);
        holder.dashh.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        holder.header.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ProductSans-Regular.ttf"));
        holder.header.setText(data.getBrandModel());
        // Reference to an image file in Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("VehiclePhotos").child(data.getVUrl() + ".png");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).centerCrop().fit().into(holder.fleet_image, new Callback() {
                    @Override
                    public void onSuccess() {
                        setViewAnimation(context, holder.fleet_image);
                        holder.fleet_image.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("FirebaseError", "PicassoDownload", exception);
            }
        });

        setRecyclerViewAnimation(context, holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FleetViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView fleet_image;
        RelativeLayout dashh;
        AppCompatTextView header;

        public FleetViewHolder(View itemView) {
            super(itemView);
            fleet_image = itemView.findViewById(R.id.fleet);
            dashh = itemView.findViewById(R.id.dasshed);
            header = itemView.findViewById(R.id.headerText);
        }

    }
}
