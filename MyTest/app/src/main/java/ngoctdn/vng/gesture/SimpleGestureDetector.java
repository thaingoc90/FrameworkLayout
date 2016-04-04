package ngoctdn.vng.gesture;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by ngoctdn on 3/21/2016.
 */
public class SimpleGestureDetector {

    private GestureDetector mGesture;
    private OnGestureListener mListener;

    public void SimpleGestureDetector(Context context) {
        mGesture = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (mListener != null) {
                    mListener.onFling(e2.getX(), e2.getY(), velocityX, velocityY);
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    public void setListener(OnGestureListener listener) {
        mListener = listener;
    }
}
