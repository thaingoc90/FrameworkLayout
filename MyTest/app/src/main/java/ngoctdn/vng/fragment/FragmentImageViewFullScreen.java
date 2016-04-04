package ngoctdn.vng.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ngoctdn.vng.activity.MainActivity;
import ngoctdn.vng.activity.R;
import ngoctdn.vng.adapter.ImagePagerAdapter;

/**
 * Created by ngoctdn on 3/22/2016.
 */
public class FragmentImageViewFullScreen extends Fragment {

    public static final String LOG_TAG = "FragmentImageViewFullScreen";
    private int mPosition = 0;
    private List<String> lstPhotos;
    private ViewPager mViewPager;
    private RelativeLayout navigationBar;
    private ImageView backBtn;
    private TextView descTv;
    private ImagePagerAdapter pagerAdapter;

    @SuppressLint("ValidFragment")
    public FragmentImageViewFullScreen(int pos, List<String> datas) {
        if (datas == null) {
            lstPhotos = new ArrayList<>();
        } else {
            lstPhotos = new ArrayList<>(datas);
        }
        mPosition = pos;


    }

    public FragmentImageViewFullScreen() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View imageShowView = inflater.inflate(R.layout.fragment_full_image_view, container, false);
        navigationBar = (RelativeLayout) imageShowView.findViewById(R.id.navigation_bar_full_image);
        backBtn = (ImageView) navigationBar.findViewById(R.id.navigation_bar_back_btn);
        descTv = (TextView) navigationBar.findViewById(R.id.navigation_bar_desc);
        mViewPager = (ViewPager) imageShowView.findViewById(R.id.viewpager_full_image);
        pagerAdapter = new ImagePagerAdapter(this, lstPhotos, mViewPager, navigationBar);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(mPosition);
        String desc = (mPosition + 1) + "/" + pagerAdapter.getCount();
        descTv.setText(desc);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                String desc = (mViewPager.getCurrentItem() + 1) + "/" + pagerAdapter.getCount();
                descTv.setText(desc);

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });

        getBaseActivity().showHideFloatingBtn(false);
        getBaseActivity().lockDrawerLayout(true);
        return imageShowView;
    }

    public void closeFragment() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.remove(FragmentImageViewFullScreen.this);
                    ft.commit();
                }
            });
        }
    }

    public MainActivity getBaseActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public void onDestroyView() {
        mViewPager.clearOnPageChangeListeners();
        getBaseActivity().showHideFloatingBtn(true);
        getBaseActivity().lockDrawerLayout(false);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
