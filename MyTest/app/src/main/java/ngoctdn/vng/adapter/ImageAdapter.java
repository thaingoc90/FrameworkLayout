package ngoctdn.vng.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.callback.ImageOptions;

import java.util.ArrayList;
import java.util.List;

import ngoctdn.vng.activity.R;

/**
 * Created by NhaN on 3/4/2015.
 */
public class ImageAdapter extends BaseAdapter {

    Context context;
    List<String> lstUrl = new ArrayList<>();
    ListView listView;

    private AQuery aq;
    private boolean isScrolling = false;
    Bitmap preset;
    Bitmap retry ;
    private boolean[] loadFail ;



    private ImageOptions io;

    public ImageAdapter(Context context, List<String> lstUrl, ListView listView) {
        this.context = context;
        this.lstUrl = lstUrl;
        this.listView = listView;
        aq = new AQuery(context);
        preset = BitmapFactory.decodeResource(context.getResources(), R.drawable.holder);
        retry = BitmapFactory.decodeResource(context.getResources(), R.drawable.holder);

        io = new ImageOptions();
        io.fileCache = true;
        io.memCache = true;
        io.preset = preset;
        io.targetWidth = 400;
        io.round = 10;
        io.animation = AQuery.FADE_IN;
        loadFail = new boolean[lstUrl.size()];
    }

    public void setScrollingStatus(boolean value) {
        this.isScrolling = value;
    }

    static class ViewHolder {
        ProgressBar progressBar;
        ImageView image;
    }


    @Override
    public int getCount() {
        return lstUrl.size();
    }

    @Override
    public Object getItem(int position) {
        return lstUrl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.img_id);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final String url = (String) getItem(position);

        if (loadFail[position]) {
            viewHolder.progressBar.setVisibility(View.GONE);
            viewHolder.image.setImageBitmap(retry);
        } else {
            viewHolder.progressBar.setVisibility(View.GONE);
            aq.id(viewHolder.image).image(preset);

            if (isScrolling) {
                if (BitmapAjaxCallback.isMemoryCached(url))
                    aq.id(viewHolder.image).image(url, io);
                else if (aq.getCachedFile(url) == null)
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
            } else {
                if (aq.getCachedFile(url) == null && !BitmapAjaxCallback.isMemoryCached(url)) {
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }

                BitmapAjaxCallback cb = new BitmapAjaxCallback(){
                  @Override
                  protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                      viewHolder.progressBar.setVisibility(View.GONE);
                      if (bm == null){
                          iv.setImageBitmap(retry);
                          loadFail[position] = true ;
                      } else {
                          iv.setImageBitmap(bm);
                      }
                  }
                };

                cb.url(url).preset(preset).fileCache(true).memCache(true).round(40).animation(AQuery.FADE_IN).targetWidth(400);
                aq.id(viewHolder.image).image(cb);
            }
        }

        viewHolder.image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if (loadFail[position]){
                      loadFail[position] = false ;
                      notifyDataSetChanged();
              }
            }
        });

        return convertView;
    }

}
