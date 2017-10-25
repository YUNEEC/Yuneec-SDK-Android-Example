package com.yuneec.example.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by sushmas on 10/25/17.
 */

public class CustomEditTextView extends android.support.v7.widget.AppCompatEditText {

    public CustomEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Lato-Regular.ttf");
        setTypeface(tf, 1);
    }
}
