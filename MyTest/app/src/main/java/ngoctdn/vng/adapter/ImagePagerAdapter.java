package ngoctdn.vng.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import java.util.LinkedList;
import java.util.List;

import ngoctdn.vng.fragment.FragmentImageViewFullScreen;
import ngoctdn.vng.activity.R;
import ngoctdn.vng.gesture.DragGestureDetector;
import ngoctdn.vng.gesture.OnGestureListener;
import ngoctdn.vng.utils.Log;
import ngoctdn.vng.utils.Utils;

/**
 * Created by ngoctdn on 6/24/2015.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private static final String VIEW_TAG_PREFIX = "ImagePager";
    private static final float MAX_VELOCITY_TO_SCROLLING_AWAY = 1000;
    private static final int SIXTY_FPS_INTERVAL = 1000 / 60;
    private static Interpolator sInterpolator = new AccelerateDecelerateInterpolator();
    private static int mMaxDistanceForSwipeUpOrDown = 100;

    private List<String> mListPhotos;
    private Context mContext;
    private Fragment mFragment;
    private LayoutInflater mLayoutInflater;
    private ViewPager mViewPager;
    private View mActionBar, mBackground;
    private AQuery mAQ;
    private ImageOptions imageOpt;
    private GestureDetector mGestureDetector;
    private DragGestureDetector mDragDectector;
    private DragListener mDragListener;
    private int mScreenHeight;
    private boolean isDraggingUpOrDown = false;

    public static final String LOG_TAG = "ImagePagerAdapter";

    public ImagePagerAdapter(Fragment fragment, List<String> resource,
                             ViewPager viewPager, View actionBar) {
        mFragment = fragment;
        mContext = fragment.getActivity();
        mViewPager = viewPager;
        mActionBar = actionBar;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (resource != null) {
            mListPhotos = resource;
        } else {
            mListPhotos = new LinkedList<>();
        }
        mGestureDetector = new GestureDetector(mContext, new SingleTapConfirm());
        mDragDectector = new DragGestureDetector(mContext);
        mDragListener = new DragListener();
        mDragDectector.setOnGestureListener(mDragListener);
        mScreenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        mMaxDistanceForSwipeUpOrDown = (int) (mScreenHeight / 6.0f);

        mAQ = new AQuery(mContext);
        imageOpt = new ImageOptions();
        imageOpt.memCache = true;
        imageOpt.fileCache = true;
        imageOpt.animation = AQuery.FADE_IN_NETWORK;
        imageOpt.targetWidth = 920;
    }

    @Override
    public int getCount() {
        return mListPhotos != null ? mListPhotos.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(LOG_TAG, "instantiateItem:" + position);
        View itemView = mLayoutInflater.inflate(R.layout.image_pager_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image_pager_item_iv);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (mGestureDetector != null && mGestureDetector.onTouchEvent(ev)) {
                    return true;
                }

                if (Utils.isCompatible(11) &&
                        mDragDectector != null && mDragDectector.onTouchEvent(ev)) {
                    return true;
                }
                return false;
            }
        });
        String photoUrl = mListPhotos.get(position);
        mAQ.id(imageView).image(photoUrl, imageOpt);
        itemView.setTag(VIEW_TAG_PREFIX + position);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d(LOG_TAG, "destroyItem: " + position);
        container.removeView((LinearLayout) object);
    }

    private View getCurrentView() {
        if (mViewPager == null) {
            return null;
        }
        int position = mViewPager.getCurrentItem();
        if (position < 0 || position >= getCount()) {
            return null;
        }
        return mViewPager.findViewWithTag(VIEW_TAG_PREFIX + position);
    }

    private View getBackgroundView() {
        if (mBackground != null) {
            return mBackground;
        }
        if (mViewPager == null) {
            return null;
        }
        ViewParent parent = mViewPager.getParent();
        if (parent != null && parent instanceof View) {
            mBackground = (View) parent;
        }
        return mBackground;
    }

    private boolean isScrollingUp(float lastY, float currentY, float velocityY) {
        return (lastY > currentY && isDraggingUpOrDown &&
                ((lastY - currentY >= mMaxDistanceForSwipeUpOrDown) ||
                        velocityY > MAX_VELOCITY_TO_SCROLLING_AWAY));
    }

    private boolean isScrollingDown(float lastY, float currentY, float velocityY) {
        return (currentY > lastY && isDraggingUpOrDown &&
                ((currentY - lastY >= mMaxDistanceForSwipeUpOrDown) ||
                        velocityY > MAX_VELOCITY_TO_SCROLLING_AWAY));
    }

    private void dragUpOrDown(final float lastY, final float currentY) {
        final View v = getCurrentView();
        if (v == null) {
            return;
        }
        v.post(new Runnable() {
            @Override
            public void run() {
                if (lastY == currentY) {
                    return;
                }
                try {
                    isDraggingUpOrDown = true;
                    float translationY = v.getY();
                    float dy = currentY - lastY;
                    float halfScreenHeight = mScreenHeight / 2.0f;
                    float alpha = 1 - (Math.abs(translationY) / halfScreenHeight);
                    v.setY(translationY + dy);
                    mBackground = getBackgroundView();
                    if (mBackground != null && alpha >= 0 && alpha <= 1) {
                        mBackground.setAlpha(alpha);
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "dragUpOrDown: exception " + e.toString());
                }
            }
        });
    }

    private class DragListener implements OnGestureListener {
        @Override
        public void onDrag(float lastY, float currentY, float dx, float dy) {
            if (Math.abs(dy) > Math.abs(dx)) {
                dragUpOrDown(lastY, currentY);
            }
        }

        @Override
        public void onFling(float startX, float startY, float velocityX, float velocityY) {
        }

        @Override
        public void onRelease(float firstY, float currentY, float velocityY) {
//            Log.d(LOG_TAG, "onRelease");
            final View v = getCurrentView();
            if (v == null) {
                return;
            }
            if (isScrollingUp(firstY, currentY, velocityY)) {
                v.post(new AnimatedMoveRunnable(-mScreenHeight, 0.0f, 200));
            } else if (isScrollingDown(firstY, currentY, velocityY)) {
                v.post(new AnimatedMoveRunnable(mScreenHeight, 0.0f, 200));
            } else {
                long duration = 100l;
                v.post(new AnimatedMoveRunnable(0.0f, 1.0f, duration));
            }
        }
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            if (mActionBar != null) {
                if (mActionBar.getVisibility() != View.VISIBLE) {
                    mActionBar.setVisibility(View.VISIBLE);
                } else {
                    mActionBar.setVisibility(View.GONE);
                }
            }
            return true;
        }
    }

    private class AnimatedMoveRunnable implements Runnable {

        private float startY, endY;
        private long duration;
        private long mStartTime;
        private DecelerateInterpolator mDecInterpolator;
        private float startAlpha, endAlpha;

        public AnimatedMoveRunnable(float toY, float toAlpha, long duration) {
            View imageView = getCurrentView();
            if (imageView == null) {
                startY = 0;
            } else {
                startY = imageView.getTranslationY();
            }
            endY = toY;
            mBackground = getBackgroundView();
            if (mBackground != null) {
                startAlpha = mBackground.getAlpha();
            }
            endAlpha = toAlpha;
            mStartTime = System.currentTimeMillis();
            this.duration = duration;
            mDecInterpolator = new DecelerateInterpolator();
        }

        @Override
        public void run() {
            View imageView = getCurrentView();
            if (imageView == null) {
                return;
            }
            float t = interpolate();
            float translate = startY + t * (endY - startY);
            float alpha = startAlpha + t * (endAlpha - startAlpha);
            imageView.setY(translate);
            mBackground = getBackgroundView();
            if (mBackground != null) {
                mBackground.setAlpha(alpha);
            }
            if (t < 1f) {
                postOnAnimation(imageView, this);
            }
            if ((System.currentTimeMillis() - mStartTime) >= duration && endY != 0.0f && isDraggingUpOrDown) {
                isDraggingUpOrDown = false;
                if (mFragment instanceof FragmentImageViewFullScreen) {
                    ((FragmentImageViewFullScreen) mFragment).closeFragment();
                }
                return;
            }
            if ((System.currentTimeMillis() - mStartTime) >= duration && endY == 0.0f && isDraggingUpOrDown) {
                isDraggingUpOrDown = false;
            }
        }

        private float interpolate() {
            float t = 1f * (System.currentTimeMillis() - mStartTime) / duration;
            t = Math.min(1f, t);
            if (endY == 0.0f) {
                t = mDecInterpolator.getInterpolation(t);
            } else {
                t = sInterpolator.getInterpolation(t);
            }

            return t;
        }
    }

    public static void postOnAnimation(View view, Runnable runnable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.postOnAnimation(runnable);
        } else {
            view.postDelayed(runnable, SIXTY_FPS_INTERVAL);
        }
    }
}
