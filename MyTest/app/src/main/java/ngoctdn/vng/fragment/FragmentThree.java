package ngoctdn.vng.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ngoctdn.vng.activity.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentThree extends MyBaseFragment {

    public FragmentThree() {
        super("ThreeFragment", R.layout.fragment_three);
    }

    @Override
    public void onInitView(View view) {
        super.onInitView(view);
//        Toolbar fragToolbar = (Toolbar) view.findViewById(R.id.toolbar_three);
//        fragToolbar.setTitle("Nếu ngày xưa");
//        getBaseActivity().setSupportActionBar(fragToolbar);
    }
}
