package aabdrrstvy.vehiclehub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import aabdrrstvy.vehiclehub.R;
import aabdrrstvy.vehiclehub.datas.DrawerItemData;
import aabdrrstvy.vehiclehub.views.Drawer.AdityaNavigationLayout;

/**
 * Created by adityagupta on 10/12/17.
 */

public class DrawerItemsAdapter extends RecyclerView.Adapter<DrawerItemsAdapter.DrawerItemHolder> {

    Context context;
    List<DrawerItemData> list;
    AdityaNavigationLayout mainLayout;
    FragmentManager fragmentManager;
    ActionBar actionBar;
    int index = 0;

    public DrawerItemsAdapter(Context context, List<DrawerItemData> list, AdityaNavigationLayout mainLayout, FragmentManager fragmentManager, ActionBar actionBar) {
        this.context = context;
        this.list = list;
        this.mainLayout = mainLayout;
        this.fragmentManager = fragmentManager;
        this.actionBar = actionBar;
    }

    @Override
    public DrawerItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false);
        return new DrawerItemHolder(v);
    }

    @Override
    public void onBindViewHolder(final DrawerItemHolder holder, final int position) {
        final DrawerItemData data = list.get(position);

        holder.icon.setImageBitmap(data.image);
        holder.title.setText(data.title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setTitle(data.actiontitle);
                mainLayout.closeDrawer();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeFragment(data.fragment);
                    }
                }, 300);
                index = position;
                notifyDataSetChanged();
            }
        });

        if (data.actiontitle.equals("Tips")) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.topMargin = 56;
            holder.itemView.setLayoutParams(params);
        }

        if (index == position) {
            holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.title.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.drawer_default), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.title.setTextColor(context.getResources().getColor(R.color.drawer_text_default));
        }
    }


    public void changeFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.frag_container, fragment).addToBackStack(null).commit();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DrawerItemHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;

        public DrawerItemHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
        }

    }
}
