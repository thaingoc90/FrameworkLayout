package ngoctdn.vng.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.androidquery.AQuery;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ngoctdn.vng.animations.FragmentAnimUtils;
import ngoctdn.vng.fragment.FragmentFour;
import ngoctdn.vng.fragment.FragmentImageViewFullScreen;
import ngoctdn.vng.fragment.FragmentMain;
import ngoctdn.vng.fragment.FragmentOne;
import ngoctdn.vng.fragment.FragmentThree;
import ngoctdn.vng.fragment.FragmentTwo;
import ngoctdn.vng.fragment.MyBaseFragment;
import ngoctdn.vng.utils.Log;
import ngoctdn.vng.utils.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    public static final String LOG_TAG = "MainActivity";
    private FragmentManager mFragmentManager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private FloatingActionButton mFloatingBtn;
    private int mCountPressBack = 0;
    private Handler mHandler;
    private Map<Integer, Class> mFragmentMaps;
    private Animator mCurrentAnimator;
    private ImageView mFakeExpandedImage;
    private AQuery mAQuery;
    private ServicePhoneStateReceiver phoneConnectivityReceiver;
    private TelephonyManager telephonyManager;

    // Listeners
    private final View.OnClickListener mSnackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(LOG_TAG, "Uni");
        }
    };

    private View.OnClickListener mUpIndicatorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onSupportNavigateUp();
        }
    };

    private class ServicePhoneStateReceiver extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.i(LOG_TAG, "Call state has changed !" + state + " : "
                    + incomingNumber);
        }
    }

    private void registerBroadcast() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (phoneConnectivityReceiver == null && telephonyManager != null) {
            Log.i(LOG_TAG, "Listen for phone state ");
            phoneConnectivityReceiver = new ServicePhoneStateReceiver();
            telephonyManager.listen(phoneConnectivityReceiver,
                    PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    private void unregisterBroadcasts() {
        if (phoneConnectivityReceiver != null && telephonyManager != null) {
            Log.i(LOG_TAG, "Unregister telephony receiver");
            telephonyManager.listen(phoneConnectivityReceiver,
                    PhoneStateListener.LISTEN_NONE);
            phoneConnectivityReceiver = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler(getMainLooper());
        mFragmentManager = getSupportFragmentManager();
        mAQuery = new AQuery(this);

        mFloatingBtn = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Undo", mSnackListener).setActionTextColor(getResources().getColor(android.R.color.holo_red_dark)).show();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mFragmentManager.addOnBackStackChangedListener(this);

        mFakeExpandedImage = (ImageView) findViewById(R.id.main_fake_expanded_image);

        mFragmentMaps = new HashMap<>();
        mFragmentMaps.put(null, FragmentMain.class);
        mFragmentMaps.put(R.id.nav_camera, FragmentOne.class);
        mFragmentMaps.put(R.id.nav_gallery, FragmentTwo.class);
        mFragmentMaps.put(R.id.nav_slideshow, FragmentThree.class);
        mFragmentMaps.put(R.id.nav_manage, FragmentFour.class);

        Class mainClazz = getFragmentClassFromMaps(null);
        MyBaseFragment mainFragment = getFragmentInstanceFromClass(mainClazz);
        showFragment(mainFragment, true);
//        registerBroadcast();
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "Ngoctdn: onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "Ngoctdn: onResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "Ngoctdn: onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "Ngoctdn: onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        unregisterBroadcasts();
        Log.d(LOG_TAG, "Ngoctdn: onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        shouldDisplayHomeUp();
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    @Override
    public boolean onSupportNavigateUp() {
        int numEntry = getNumBackStackEntry();
        if (numEntry <= 0) {
            return false;
        }
        mFragmentManager.popBackStackImmediate();
        return true;
    }

    private void shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        int numEntry = getNumBackStackEntry();
        mDrawerToggle.setDrawerIndicatorEnabled(numEntry == 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(numEntry > 0);
        if (numEntry > 0) {
            mDrawerToggle.setToolbarNavigationClickListener(mUpIndicatorListener);
        }
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        if (mCurrentAnimator != null) {
            return;
        }

        Fragment fragment = getFragmentFullScreen();
        if (fragment != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.remove(fragment);
            ft.commit();
            return;
        }

        int countEntry = getNumBackStackEntry();
        if (countEntry > 0) {
            onSupportNavigateUp();
        } else {
            mCountPressBack++;
            if (mCountPressBack == 1) {
                Utils.showToastShort("Press again to exit");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCountPressBack = 0;
                    }
                }, 1500);
            } else {
                super.onBackPressed();
            }
        }
    }

    // show menu settings on actionbar
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        MyBaseFragment activeFragment = getActiveBaseFragments();
        Class clazz = getFragmentClassFromMaps(id);
        if (clazz == null || activeFragment == null || activeFragment.getClass() == clazz) {
            return true;
        }
        MyBaseFragment seletedFragment = getFragmentInstanceFromClass(clazz);
        if (seletedFragment != null) {
            showFragment(seletedFragment, false);
        }

        item.setChecked(true);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        MyBaseFragment fragment = getActiveBaseFragments();
        if (fragment != null) {
            fragment.onKeyEvent(keyCode, event);
        }
        return super.onKeyUp(keyCode, event);
    }

    public int getNumBackStackEntry() {
        return mFragmentManager != null ? mFragmentManager.getBackStackEntryCount() : 0;
    }

    private void popAllFragments() {
        // Clear all back stack.
        int backStackCount = getNumBackStackEntry();
        for (int i = 0; i < backStackCount; i++) {
            // Get the back stack fragment id.
            String backStackTag = mFragmentManager.getBackStackEntryAt(i).getName();
            int backStackId = mFragmentManager.getBackStackEntryAt(i).getId();
            MyBaseFragment fragment = (MyBaseFragment) mFragmentManager.findFragmentByTag(backStackTag);
            if (fragment != null) {
                fragment.setDisableFragmentAnimation(true);
            }
            mFragmentManager.popBackStackImmediate(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            if (fragment != null) {
                fragment.setDisableFragmentAnimation(false);
            }
        }
    }

    private MyBaseFragment getActiveBaseFragments() {
        if (getNumBackStackEntry() == 0) {
            return (MyBaseFragment) mFragmentManager.findFragmentById(R.id.main_fragment_layout);
        }
        String tag = mFragmentManager.getBackStackEntryAt(getNumBackStackEntry() - 1).getName();
        return (MyBaseFragment)mFragmentManager.findFragmentByTag(tag);
    }

    private MyBaseFragment getMainBaseFragment() {
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments.size() >= 2 && fragments.get(0) != null) {
            return (MyBaseFragment) fragments.get(0);
        }
        return getActiveBaseFragments();
    }

    public void showFragment(MyBaseFragment fragment, boolean isFirst) {
        Class mainClazz = getFragmentClassFromMaps(null);
        if (fragment == null || mainClazz == null) {
            return;
        }
        if (!isFirst && mainClazz == fragment.getClass()) {
            return;
        }

        Log.d(LOG_TAG, "showFragment id = " + fragment.getId() + " -- name = " + fragment.getClass().getName());

        if (isFirst) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.add(R.id.main_fragment_layout, fragment, fragment.getTagName());
            ft.commit();
        } else {
            popAllFragments();
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            FragmentAnimUtils.addAnim(ft, FragmentAnimUtils.SLIDE_HORIZONTAL);
            ft.add(R.id.main_fragment_layout, fragment, fragment.getTagName());
            ft.addToBackStack(fragment.getClass().getName());
            ft.commit();
        }
    }

    public Fragment getFragmentFullScreen() {
        return mFragmentManager.findFragmentById(R.id.fragment_full_screen);
    }

    public void showFragmentFullscreen(int pos, List<String> datas, View currentView) {
        View fullview = findViewById(R.id.main_container);
        zoomInImageFromThumb(fullview, currentView, mFakeExpandedImage, pos, datas);
    }

    /**
     * Show / hide floatingActionButton
     *
     * @param show
     */
    public void showHideFloatingBtn(boolean show) {
        if (show) {
            mFloatingBtn.show();
        } else {
            mFloatingBtn.hide();
        }
    }

    /**
     * Enable / Disable Navigation View
     *
     * @param lock
     */
    public void lockDrawerLayout(boolean lock) {
        if (lock) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    private void clearSelectedItem() {
        if (mNavigationView != null && mNavigationView.getMenu() != null) {
            for (int i = 0; i < mNavigationView.getMenu().size(); i++) {
                mNavigationView.getMenu().getItem(i).setChecked(false);
            }
        }
    }

    public void checkItemOnMenu(MyBaseFragment fragments) {
        if (mFragmentMaps == null || mNavigationView == null ||
                mNavigationView.getMenu() == null) {
            return;
        }
        Integer result = null;
        for (Map.Entry<Integer, Class> entry : mFragmentMaps.entrySet()) {
            if (entry.getValue() == fragments.getClass()) {
                result = entry.getKey();
                if (result == null) {
                    // get main fragment, show clear check menuitem;
                    clearSelectedItem();
                    return;
                }
                break;
            }
        }
        if (result != null) {
            for (int i = 0; i < mNavigationView.getMenu().size(); i++) {
                if (mNavigationView.getMenu().getItem(i).getItemId() == result.intValue()) {
                    mNavigationView.getMenu().getItem(i).setChecked(true);
                } else {
                    mNavigationView.getMenu().getItem(i).setChecked(false);
                }
            }
        }
    }

    private Class getFragmentClassFromMaps(Integer key) {
        if (mFragmentMaps == null) {
            return null;
        }
        Class clazz = mFragmentMaps.get(key);
        if (clazz != null && MyBaseFragment.class.isAssignableFrom(clazz)) {
            return clazz;
        }
        return null;
    }

    private MyBaseFragment getFragmentInstanceFromClass(Class clazz) {
        try {
            if (clazz != null && MyBaseFragment.class.isAssignableFrom(clazz)) {
                Constructor<MyBaseFragment> ctor = clazz.getConstructor();
                return ctor.newInstance();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "getFragmentInstanceFromClass error:" + e.toString());
        }
        return null;
    }

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private void zoomInImageFromThumb(final View viewParent, final View thumbView,
                                      final ImageView expandedImageView,
                                      final int pos, final List<String> datas) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        String urlImage = datas.get(pos);

        mAQuery.id(expandedImageView).image(urlImage, Utils.getImageOptions());

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        viewParent.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        expandedImageView.setVisibility(View.VISIBLE);
        ((View) expandedImageView.getParent()).setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        final int mShortAnimationDuration = getResources().
                getInteger(android.R.integer.config_shortAnimTime);
        animatorSet.setDuration(mShortAnimationDuration);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCurrentAnimator = null;
                        Fragment fragmentFullScreen = getFragmentFullScreen();
                        if (fragmentFullScreen == null) {
                            FragmentTransaction ft = mFragmentManager.beginTransaction();
                            fragmentFullScreen = new FragmentImageViewFullScreen(pos, datas);
                            ft.add(R.id.fragment_full_screen, fragmentFullScreen);
                            ft.commit();
                        }
                        ((View) expandedImageView.getParent()).setVisibility(View.GONE);
                        expandedImageView.setVisibility(View.GONE);
                    }
                }, 150);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        animatorSet.start();
        mCurrentAnimator = animatorSet;

    }

    private void zoomOutImageToThumb(final View viewParent, final View thumbView,
                                     final ImageView expandedImageView,
                                     final int pos, final List<String> datas) {
        //        // Upon clicking the zoomed-in image, it should zoom back down
//        // to the original bounds and show the thumbnail instead of
//        // the expanded image.
//        final float startScaleFinal = startScale;
//        expandedImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mCurrentAnimator != null) {
//                    mCurrentAnimator.cancel();
//                }
//
//                // Animate the four positioning/sizing properties in parallel,
//                // back to their original values.
//                AnimatorSet animatorSet1 = new AnimatorSet();
//                animatorSet1.play(ObjectAnimator
//                        .ofFloat(expandedImageView, View.X, startBounds.left))
//                        .with(ObjectAnimator.ofFloat(expandedImageView,
//                                View.Y, startBounds.top))
//                        .with(ObjectAnimator.ofFloat(expandedImageView,
//                                View.SCALE_X, startScaleFinal))
//                        .with(ObjectAnimator.ofFloat(expandedImageView,
//                                View.SCALE_Y, startScaleFinal));
//                animatorSet1.setDuration(mShortAnimationDuration);
//                animatorSet1.setInterpolator(new DecelerateInterpolator());
//                animatorSet1.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        thumbView.setAlpha(1f);
//                        expandedImageView.setVisibility(View.GONE);
//                        mCurrentAnimator = null;
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//                        thumbView.setAlpha(1f);
//                        expandedImageView.setVisibility(View.GONE);
//                        mCurrentAnimator = null;
//                    }
//                });
//                animatorSet1.start();
//                mCurrentAnimator = animatorSet1;
//            }
//        });
    }


    // DEBUG FUNC
    private void debugFragment() {
        View mainview = findViewById(R.id.fragment_main);
        View oneview = findViewById(R.id.fragment_one);
        View twoview = findViewById(R.id.fragment_two);
        View view1 = findViewById(R.id.main_fragment_layout);
        View threeView = findViewById(R.id.fragment_three);
        View fourView = findViewById(R.id.fragment_four);
        List<View> lstView = new LinkedList<>();
        lstView.add(mainview);
        lstView.add(oneview);
        lstView.add(twoview);
        lstView.add(threeView);
        lstView.add(fourView);
        for (int i = 0; i < lstView.size(); i++) {
            View view = lstView.get(i);
            if (view == null) {
                Log.d(LOG_TAG, getNameFromIndex(i) + " is null");
            } else if (view.getParent() == null) {
                Log.d(LOG_TAG, getNameFromIndex(i) + " parent is null" + view.getId());
            } else {
                Log.d(LOG_TAG, getNameFromIndex(i) + " - " + view.getId() + " - " + ((ViewGroup) view.getParent()).getId());
            }
        }
        Log.d(LOG_TAG, "View: " + view1.getId());
    }

    private String getNameFromIndex(int ind) {
        return "View " + String.valueOf(ind);
    }

//
//
//    static {
//        System.loadLibrary("ngocnative");
//    }
//
//    // NATIVE FUNC
//    public native void func_test1();
//
//    public void funccallback(int a) {
//        Log.d(LOG_TAG, "funccallback: " + a);
//    }

}
