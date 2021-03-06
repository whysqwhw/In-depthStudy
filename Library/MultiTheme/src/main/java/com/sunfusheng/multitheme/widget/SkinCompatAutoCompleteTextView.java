package com.sunfusheng.multitheme.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;

import com.sunfusheng.multitheme.R;
import com.sunfusheng.multitheme.res.SkinCompatResources;

import static com.sunfusheng.multitheme.widget.SkinCompatHelper.INVALID_ID;


public class SkinCompatAutoCompleteTextView extends AppCompatAutoCompleteTextView implements SkinCompatSupportable {
    private static final int[] TINT_ATTRS = {
            android.R.attr.popupBackground
    };
    private int mDropDownBackgroundResId = INVALID_ID;
    private SkinCompatTextHelper mTextHelper;
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public SkinCompatAutoCompleteTextView(Context context) {
        this(context, null);
    }

    public SkinCompatAutoCompleteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.autoCompleteTextViewStyle);
    }

    public SkinCompatAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                TINT_ATTRS, defStyleAttr, 0);
        if (a.hasValue(0)) {
            mDropDownBackgroundResId = a.getResourceId(0, INVALID_ID);
        }
        a.recycle();
        applyDropDownBackgroundResource();
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
        mTextHelper = new SkinCompatTextHelper(this);
        mTextHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    @Override
    public void setDropDownBackgroundResource(@DrawableRes int resId) {
        super.setDropDownBackgroundResource(resId);
        mDropDownBackgroundResId = resId;
        applyDropDownBackgroundResource();
    }

    private void applyDropDownBackgroundResource() {
        mDropDownBackgroundResId = SkinCompatHelper.checkResourceId(mDropDownBackgroundResId);
        if (mDropDownBackgroundResId != INVALID_ID) {
            String typeName = getResources().getResourceTypeName(mDropDownBackgroundResId);
            if ("color".equals(typeName)) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    int color = SkinCompatResources.getInstance().getColor(mDropDownBackgroundResId);
                    setDrawingCacheBackgroundColor(color);
                } else {
                    ColorStateList colorStateList =
                            SkinCompatResources.getInstance().getColorStateList(mDropDownBackgroundResId);
                    Drawable drawable = getDropDownBackground();
                    DrawableCompat.setTintList(drawable, colorStateList);
                    setDropDownBackgroundDrawable(drawable);
                }
            } else if ("drawable".equals(typeName)) {
                Drawable drawable = SkinCompatResources.getInstance().getDrawable(mDropDownBackgroundResId);
                setDropDownBackgroundDrawable(drawable);
            } else if ("mipmap".equals(typeName)) {
                Drawable drawable = SkinCompatResources.getInstance().getMipmap(mDropDownBackgroundResId);
                setDropDownBackgroundDrawable(drawable);
            }
        }
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resId) {
        super.setBackgroundResource(resId);
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(resId);
        }
    }

    @Override
    public void setTextAppearance(Context context, int resId) {
        super.setTextAppearance(context, resId);
        if (mTextHelper != null) {
            mTextHelper.onSetTextAppearance(context, resId);
        }
    }

    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
        if (mTextHelper != null) {
            mTextHelper.applySkin();
        }
        applyDropDownBackgroundResource();
    }
}
