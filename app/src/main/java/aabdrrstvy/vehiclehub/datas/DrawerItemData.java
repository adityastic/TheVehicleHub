package aabdrrstvy.vehiclehub.datas;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

public class DrawerItemData {

    public Bitmap image;
    public String title, actiontitle;
    public Fragment fragment;

    public DrawerItemData(Bitmap image, String title, String actiontitle, Fragment fragment) {
        this.image = image;
        this.title = title;
        this.actiontitle = actiontitle;
        this.fragment = fragment;
    }
}
