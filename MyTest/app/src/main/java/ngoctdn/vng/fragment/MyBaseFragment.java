package ngoctdn.vng.fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;

import ngoctdn.vng.activity.MainActivity;
import ngoctdn.vng.animations.FragmentAnimUtils;
import ngoctdn.vng.app.MainApplication;
import ngoctdn.vng.utils.Utils;
import ngoctdn.vng.utils.Log;

/**
 * Created by ngoctdn on 3/9/2016.
 */
@SuppressLint("ValidFragment")
public class MyBaseFragment extends Fragment {

    public static final String LOG_TAG = "MyBaseFragment";
    private static final int SWIGE_LEFT_MIN = 100;
    private String mTitle;
    private int mViewResource;
    private GestureDetector gesture;
    private int mMinimumVelocity;

    private boolean mDisableFragmentAnimation = false;

    public MyBaseFragment(String title, int viewResource) {
        this.mTitle = title;
        this.mViewResource = viewResource;
        ViewConfiguration configuration = Utils.getViewConfiguration();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {

                        Log.i(LOG_TAG, getTagName() + " -- onFling");
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > Utils.SWIPE_MAX_OFF_PATH) {
                                Log.i(LOG_TAG, getTagName() + " -- scroll");
                                return false;
                            }
                            if (e1.getX() - e2.getX() > Utils.SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > mMinimumVelocity) {
                                return swipeRightToLeft(e1, e2, velocityX, velocityY);
                            } else if (e2.getX() - e1.getX() > Utils.SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > mMinimumVelocity) {
                                return swipeLeftToRight(e1, e2, velocityX, velocityY);
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    float originViewX, originViewY, touchDownX, touchDownY;
    boolean mTrackingEvent = false, mIsAnimating = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(mViewResource, container, false);
        View.OnTouchListener listener =         new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (getBaseActivity().getNumBackStackEntry() <= 0 || mIsAnimating) {
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchDownX = event.getRawX();
                    touchDownY = event.getRawY();
                    originViewX = v.getX();
                    originViewY = v.getY();
                    mTrackingEvent = false;

                    Log.i(LOG_TAG, getTagName() + " -- MotionEvent.ACTION_DOWN " +
                            " -- " + v.getX() + " -- " + v.getY() +
                            " -- " + event.getRawX() + " -- " + event.getRawY() +
                            " -- " + event.getX() + " -- " + event.getY() +
                            " -- " + originViewX + " -- " + originViewY);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    v.animate().x(event.getRawX() + originViewX - touchDownX)
                            .setDuration(0).start();
                    mTrackingEvent = true;
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i(LOG_TAG, getTagName() + " -- MotionEvent.ACTION_UP " + mTrackingEvent);
                    if (mTrackingEvent) {
                        final float deltaX = event.getRawX() - touchDownX;
                        mIsAnimating = true;
                        if (deltaX > 400) {
                            Log.d(LOG_TAG, "go through " + deltaX + " -- " + touchDownX + " -- " +
                                    event.getRawX() + " -- " + v.getX());
                        } else {
                            Log.d(LOG_TAG, "back view " + deltaX + " -- " + v.getX());
                        }
                        v.animate().x(deltaX > 400 ? MainApplication.getScreenWidth() : originViewX)
                                .setDuration(100)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mIsAnimating = false;
                                        if (deltaX > 400) {
                                            getBaseActivity().onSupportNavigateUp();
                                        }
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {
                                        mIsAnimating = false;
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                }).start();
                    }
                    mTrackingEvent = false;
                    return true;
                }
//                return gesture.onTouchEvent(event);
                return false;
            }
        };
        //view.setOnTouchListener(listener);
        onInitView(view);
        return view;
    }

    public void onInitView(View view) {
        // Override if need init variable
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        getBaseActivity().getSupportActionBar().setTitle(getTitle());
        getBaseActivity().checkItemOnMenu(this);
        super.onResume();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (mDisableFragmentAnimation) {
            return FragmentAnimUtils.createEmptyAnimation();
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setViewResource(int mViewResource) {
        this.mViewResource = mViewResource;
    }

    public String getTagName() {
        return this.getClass().getName();
    }

    public void setDisableFragmentAnimation(boolean mDisableFragmentAnimation) {
        this.mDisableFragmentAnimation = mDisableFragmentAnimation;
    }

    public boolean swipeLeftToRight(MotionEvent e1, MotionEvent e2,
                                 float velocityX, float velocityY) {
        Log.d(LOG_TAG, getTagName() + " Left to right: " + e1.getX() + "--" + e1.getY()
                + " -- " + e2.getX() + " -- " + e2.getY());
        if (e1.getX() < SWIGE_LEFT_MIN) {
//            getBaseActivity().onSupportNavigateUp();
            return true;
        }
        return false;
    }

    public boolean swipeRightToLeft(MotionEvent e1, MotionEvent e2,
                                 float velocityX, float velocityY) {
        Log.d(LOG_TAG, getTagName() + " Left to right: " + e1.getX() + "--" + e1.getY()
                + " -- " + e2.getX() + " -- " + e2.getY());
        return false;
    }

    public MainActivity getBaseActivity() {
        return (MainActivity) getActivity();
    }

    public void onKeyEvent(int keyCode, KeyEvent event) {
        // override if need
    }
}
