package com.example.mytest.ui;

import com.example.mytest.R;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class OnScreenHint {
	int mGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    int mX, mY;
    float mHorizontalMargin;
    float mVerticalMargin;
    View mView;
    View mNextView;

    private final WindowManager.LayoutParams mParams =
            new WindowManager.LayoutParams();
    private final WindowManager mWM;
    private final Handler mHandler = new Handler();
    private OnScreenHint(Context context) {
    	mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.format = PixelFormat.TRANSLUCENT;
        //mParams.windowAnimations = R.style.Animation_OnScreenHint;
        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        mParams.setTitle("OnScreenHint");
    }
    public static OnScreenHint makeText(Context context, CharSequence text) {
        OnScreenHint result = new OnScreenHint(context);

        LayoutInflater inflate =
                (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        
        View v = inflate.inflate(R.layout.on_screen_hint, null);
        TextView tv = (TextView) v.findViewById(R.id.message);
        tv.setText(text);

        result.mNextView = v;

        return result;
    }
    public synchronized void handleShow() {
        if (true || mView != mNextView) {
            // remove the old view if necessary
            handleHide();
            mView = mNextView;
            final int gravity = mGravity;
            mParams.gravity = gravity;
            if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK)
                    == Gravity.FILL_HORIZONTAL) {
                mParams.horizontalWeight = 1.0f;
            }
            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK)
                    == Gravity.FILL_VERTICAL) {
                mParams.verticalWeight = 1.0f;
            }
            mParams.x = mX;
            mParams.y = mY;
            mParams.verticalMargin = mVerticalMargin;
            mParams.horizontalMargin = mHorizontalMargin;
            if (mView.getParent() != null) {
                mWM.removeView(mView);
            }
            mWM.addView(mView, mParams);
        }
        
    }
    public synchronized void handleHide() {
        if (mView != null) {
            // note: checking parent() just to make sure the view has
            // been added...  i have seen cases where we get here when
            // the view isn't yet added, so let's try not to crash.
            if (mView.getParent() != null) {
                mWM.removeView(mView);
            }
            mView = null;
        }
    }
        
    
}
