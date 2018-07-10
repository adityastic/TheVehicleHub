package aabdrrstvy.vehiclehub.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import aabdrrstvy.vehiclehub.R;

public class HeaderView extends AppCompatImageView {

    private int mWidthRatio;
    private int mHeightRatio;

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderView);

        try {
            mWidthRatio = typedArray.getInteger(R.styleable.HeaderView_widthRatio, 16);
            mHeightRatio = typedArray.getInteger(R.styleable.HeaderView_heightRatio, 9);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        double height = (widthMeasureSpec / mWidthRatio) * mHeightRatio;
        setMeasuredDimension(widthMeasureSpec, Double.valueOf(height).intValue());
    }

    public void setRatio(int widthRatio, int heightRatio) {
        mWidthRatio = widthRatio;
        mHeightRatio = heightRatio;
        double height = (getMeasuredWidth() / mWidthRatio) * mHeightRatio;
        setMeasuredDimension(getMeasuredWidth(), Double.valueOf(height).intValue());
    }
}
