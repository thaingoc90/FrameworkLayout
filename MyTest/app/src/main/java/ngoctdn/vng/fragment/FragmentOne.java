package ngoctdn.vng.fragment;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ngoctdn.vng.activity.R;
import ngoctdn.vng.adapter.PhotoAdapter;
import ngoctdn.vng.customview.hlistview.AbsHListView;
import ngoctdn.vng.customview.hlistview.HListView;

/**
 * Created by CPU11303-local on 3/9/2016.
 */
public class FragmentOne extends MyBaseFragment {

    private HListView mHListView;
    private PhotoAdapter mPhotoAdapter;
    private List<String> lstUrl;

    public FragmentOne() {
        super("FragmentOne", R.layout.fragment_one);
    }

    @Override
    public void onInitView(View view) {
        super.onInitView(view);
        // 10 images
        lstUrl = new ArrayList<>();
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/neg_ysfyrns/2015_03_04/3.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/neg_ysfyrns/2015_03_04/2_1.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/neg_ysfyrns/2015_03_04/4_1.jpg");
        lstUrl.add("http://farm6.static.flickr.com/5035/5802797131_a729dac808_s.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/neg_ysfyrns/2015_03_04/5.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/neg_ysfyrns/2015_03_04/6_1.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/neg_ysfyrns/2015_03_04/7_1.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/neg_ysfyrns/2015_03_04/8_1.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/neg_ysfyrns/2015_03_04/9_1.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/neg_ysfyrns/2015_03_04/10.jpg");

        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/neg_ysfyrns/2015_03_02/1.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/rugtzn/2015_03_03/a.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/rugtzn/2015_03_01/1.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/nutmjz/2015_03_04/linh.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/nutmjz/2015_03_04/dang1.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/nutmjz/2015_03_04/dang3.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/nutmjz/2015_03_04/ko1.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/nutmjz/2015_03_04/huy.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/xbhunku/2015_03_04/india2.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/xbhunku/2015_03_04/Protestersmarktheanniv007.jpg");

        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/nokarz/2015_03_05/5_zing_1.JPG");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/nokarz/2015_03_05/6_zing_1.JPG");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/nokarz/2015_03_05/7_zing_1.JPG");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/nokarz/2015_03_05/8_zing_1.JPG");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/nokarz/2015_03_05/13_zing.JPG");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/uobunia/2015_03_02/10847768_421274518034836_2692256859766251159_n.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/uobunia/2015_03_02/11010596_421274538034834_4781290091375595255_n.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/abfluua/2015_03_04/1.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/abfluua/2015_03_04/2.jpg");
        lstUrl.add("http://img.v3.news.zdn.vn/w660/Uploaded/abfluua/2015_03_04/3_cnbc.jpg");

        mHListView = (HListView) view.findViewById(R.id.one_hlistview);
//        mHListView.setItemsCanFocus(false);
//        mHListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mPhotoAdapter = new PhotoAdapter(getBaseActivity(), lstUrl);
        mHListView.setOnScrollListener(new AbsHListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsHListView view, int scrollState) {
                if (scrollState == AbsHListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    mPhotoAdapter.setScrollingStatus(false);
                    int firstVisiblePos = mHListView.getFirstVisiblePosition();
                    int lastVisiblePos = mHListView.getLastVisiblePosition();
//                    Log.d("PhotoAdapter", "Visible " + firstVisiblePos + " --> " + lastVisiblePos);
                    for (int i = firstVisiblePos; i <= lastVisiblePos; i++) {
                        View visibleView = mHListView.getChildAt(i - firstVisiblePos);
                        mPhotoAdapter.loadImage(visibleView, i);
                    }
                } else {
                    mPhotoAdapter.setScrollingStatus(true);
                }
            }

            @Override
            public void onScroll(AbsHListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mHListView.setAdapter(mPhotoAdapter);

        Button addBtn = (Button) view.findViewById(R.id.one_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int firstVisiblePos = mHListView.getFirstVisiblePosition();
                View firstVisibleView = mHListView.getChildAt(0);
                mPhotoAdapter.addImageView(lstUrl.get(0));
                if (firstVisiblePos > 0) {
                    mHListView.setSelectionFromLeft(firstVisiblePos + 1, firstVisibleView.getLeft());
                }
            }
        });
        Button delBtn = (Button) view.findViewById(R.id.one_del_btn);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posDel = 0;
                int firstVisiblePos = mHListView.getFirstVisiblePosition();
                View firstVisibleView = mHListView.getChildAt(posDel);
                mPhotoAdapter.delImageView(0);
                if (posDel < firstVisiblePos) {
                    mHListView.setSelectionFromLeft(firstVisiblePos - 1, firstVisibleView.getLeft());
                } else if (posDel == firstVisiblePos && firstVisiblePos == 0) {
                    mHListView.setSelection(0);
                }
            }
        });

    }
}
