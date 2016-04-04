package ngoctdn.vng.fragment;


import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import ngoctdn.vng.activity.R;
import ngoctdn.vng.utils.Log;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMain extends MyBaseFragment implements View.OnClickListener {

    public static final String LOG_TAG = "FragmentMain";

    public FragmentMain() {
        super("MainFragment", R.layout.fragment_main);
    }

    @Override
    public void onInitView(View view) {
        super.onInitView(view);
        Button toOne = (Button) view.findViewById(R.id.main_to_one);
        Button toTwo = (Button) view.findViewById(R.id.main_to_two);
        toOne.setOnClickListener(this);
        toTwo.setOnClickListener(this);
        View largeTv = view.findViewById(R.id.main_tv_large);
        largeTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(LOG_TAG, "Touch on textView");
                // Return true , won't process parents.
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        MyBaseFragment fragment = null;
        if (v.getId() == R.id.main_to_one) {
            fragment = new FragmentOne();
        } else if (v.getId() == R.id.main_to_two) {
            fragment = new FragmentTwo();
        }
        if (fragment != null) {
            getBaseActivity().showFragment(fragment, false);
        }
    }
}
