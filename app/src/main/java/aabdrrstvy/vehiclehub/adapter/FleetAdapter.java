package aabdrrstvy.vehiclehub.adapter;

import android.annotation.SuppressLint;
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

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import aabdrrstvy.vehiclehub.R;
import aabdrrstvy.vehiclehub.activities.FleetActivity;
import aabdrrstvy.vehiclehub.datas.FleetInfo;
import aabdrrstvy.vehiclehub.utils.firebase.FirebaseHub;

/**
 * Created by adityagupta on 10/12/17.
 */

public class FleetAdapter extends RecyclerView.Adapter<FleetAdapter.FleetViewHolder> {

    private static int lastPosition = -1;
    private Context context;
    private List<FleetInfo> list;
    private FleetActivity fleetActivity;

    private Calendar startJourney, endJourney;

    public FleetAdapter(Context context, List<FleetInfo> list, Calendar startJourney, Calendar endJourney, FleetActivity fleetActivity) {
        this.list = list;
        this.context = context;
        this.startJourney = startJourney;
        this.endJourney = endJourney;
        this.fleetActivity = fleetActivity;
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

    public void setStartJourney(Calendar startJourney) {
        this.startJourney = startJourney;
    }

    public void setEndJourney(Calendar endJourney) {
        this.endJourney = endJourney;
    }

    @NonNull
    @Override
    public FleetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_fleet_item, parent, false);
        return new FleetViewHolder(v);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FleetViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final FleetViewHolder holder, int position) {
        final FleetInfo data = list.get(position);

        holder.fleet_image.setVisibility(View.INVISIBLE);
        holder.dashh.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        holder.header.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ProductSans-Regular.ttf"));
        holder.header.setText(data.getBrandModel());
        holder.price.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ProductSans-Regular.ttf"));
        holder.price.setText("Price ₹" + data.getRate() + " per/hr");
        // Reference to an image file in Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("VehiclePhotos").child(data.getVUrl() + ".png");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).centerCrop().fit().into(holder.fleet_image, new Callback() {
                    @Override
                    public void onSuccess() {
                        setViewAnimation(context, holder.fleet_image);
                        holder.fleet_image.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("FirebaseError", "PicassoDownload", exception);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double amount = DateDifference() * data.getRate();
                FirebaseHub.addBooking(startJourney, endJourney, amount, data.getNumber());
                fleetActivity.refreshLayout();

//                Intent i = new Intent(context, SelectedFleetActivity.class);
//                context.startActivity(i);
//                ((Activity) context).overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });

        setRecyclerViewAnimation(context, holder.itemView, position);
    }

    private long DateDifference() {
        return (endJourney.getTime().getTime() - startJourney.getTime().getTime()) / TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FleetViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView fleet_image;
        RelativeLayout dashh;
        AppCompatTextView header, price;

        private FleetViewHolder(View itemView) {
            super(itemView);
            fleet_image = itemView.findViewById(R.id.fleet);
            dashh = itemView.findViewById(R.id.dasshed);
            header = itemView.findViewById(R.id.headerText);
            price = itemView.findViewById(R.id.price);
        }

    }
}
