package ngoctdn.vng.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.BitmapAjaxCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ngoctdn.vng.activity.MainActivity;
import ngoctdn.vng.activity.R;
import ngoctdn.vng.utils.Log;
import ngoctdn.vng.utils.Utils;

/**
 * Created by ngoctdn on 3/21/2016.
 */
public class PhotoAdapter extends BaseAdapter {

    public static final String LOG_TAG = "PhotoAdapter";

    private List<String> mDataObjects;
    private Context mContext;
    private AQuery mAQuery;
    private Bitmap mPreImage;
    private boolean isScrolling = false;
    private LayoutInflater mLayoutInflater;
    private Set<Integer> setImageContainBitmap;

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public PhotoAdapter(Context ctx, List<String> dataObject) {
        mContext = ctx;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDataObjects = dataObject;
        if (this.mDataObjects == null) {
            this.mDataObjects = new ArrayList<>();
        }
        mPreImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.holder);
        mAQuery = new AQuery(mContext);
        setImageContainBitmap = new HashSet<>(mDataObjects.size());
    }

    public void setScrollingStatus(boolean value) {
        this.isScrolling = value;
    }

    @Override
    public int getCount() {
        return mDataObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;
        ImageView imageView;
        if (convertView != null) {
            view = convertView;
            imageView = (ImageView) view.getTag();
        } else {
            view = mLayoutInflater.inflate(R.layout.horizontal_view_item, null, false);
            imageView = (ImageView) view.findViewById(R.id.horizontal_image_view);
            view.setTag(imageView);
        }
        String url = mDataObjects.get(position);

        if (!isScrolling || BitmapAjaxCallback.isMemoryCached(url)) {
            mAQuery.id(imageView).image(url, Utils.getImageOptions());
            setImageContainBitmap.add(position);
        } else {
            mAQuery.id(imageView).image(mPreImage);
            setImageContainBitmap.remove(position);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(LOG_TAG, "setOnClickListener " + position);
                ((MainActivity) mContext).showFragmentFullscreen(position, mDataObjects, v);
            }
        });

//        BitmapAjaxCallback cb = new BitmapAjaxCallback(){
//            @Override
//            protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
//                iv.setImageBitmap(bm);
//                Log.d(LOG_TAG, "callback: " + bm);
//            }
//        };
//
//        mAQuery.id(imageView).image(cb);
//        mAQuery.id(imageView).image(url, mImageOptions);
        return view;
    }

    public void loadImage(View view, int position) {
//        Log.d(LOG_TAG, "loadImage: " + position + " -- " + view);
        if (view == null || position < 0 || position >= getCount()) {
            return;
        }
        ImageView imageView = (ImageView) view.getTag();
        if (imageView == null) {
            imageView = (ImageView) view.findViewById(R.id.horizontal_image_view);
        }
        if (imageView == null) {
            return;
        }
        String url = mDataObjects.get(position);
        if (!setImageContainBitmap.contains(position)) {
            mAQuery.id(imageView).image(url, Utils.getImageOptions());
            setImageContainBitmap.add(position);
        }
    }

    private void notifyDataChanged() {
        setImageContainBitmap.clear();
        this.notifyDataSetChanged();
    }

    public void addImageView(String filePath) {
        try {
            mDataObjects.add(0, filePath);
            notifyDataChanged();
        } catch (Exception e) {
            Log.e(LOG_TAG, "addImageView error: " + e.toString());
        }
    }

    public void delImageView(int pos) {
        try {
            if (pos < 0 || pos >= mDataObjects.size()) {
                return;
            }
            mDataObjects.remove(pos);
            notifyDataChanged();
        } catch (Exception e) {
            Log.e(LOG_TAG, "delImageView error: " + e.toString());
        }
    }
}
