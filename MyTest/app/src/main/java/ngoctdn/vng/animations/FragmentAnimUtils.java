package ngoctdn.vng.animations;

import android.support.v4.app.FragmentTransaction;
import android.view.animation.Animation;

import ngoctdn.vng.activity.R;

/**
 * Created by ngoctdn on 3/21/2016.
 */
public class FragmentAnimUtils {

    public static final int SLIDE_HORIZONTAL = 0;
    public static final int SLIDE_VERTICAL = SLIDE_HORIZONTAL + 1;
    public static final int FADE = SLIDE_VERTICAL + 1;
    public static final int STACK = FADE + 1;
    public static final int ACCORDION = STACK + 1;
    public static final int ZOOM_SLIDE_HORIZONTAL = ACCORDION + 1;
    public static final int ZOOM_SLIDE_VERTICAL = ZOOM_SLIDE_HORIZONTAL + 1;

    /**
     * anim: getSupportFragmentManager
     * animator: getFragmentManager
     */
    public static void addAnim(FragmentTransaction ft, int type) {
        switch (type) {
            case SLIDE_HORIZONTAL:
                ft.setCustomAnimations(R.anim.slide_fragment_horizontal_right_in, R.anim.slide_fragment_horizontal_left_out,
                        R.anim.slide_fragment_horizontal_left_in, R.anim.slide_fragment_horizontal_right_out);
                break;
            case SLIDE_VERTICAL:
                ft.setCustomAnimations(R.anim.slide_fragment_vertical_right_in, R.anim.slide_fragment_vertical_left_out,
                        R.anim.slide_fragment_vertical_left_in, R.anim.slide_fragment_vertical_right_out);
                break;
            case FADE:
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case STACK:
                ft.setCustomAnimations(R.anim.stack_right_in, R.anim.stack_left_out,
                        R.anim.stack_left_in, R.anim.stack_right_out);
                break;
            case ACCORDION:
                ft.setCustomAnimations(R.anim.accordion_right_in, R.anim.accordion_left_out,
                        R.anim.accordion_left_in, R.anim.accordion_right_out);
                break;
            case ZOOM_SLIDE_HORIZONTAL:
                ft.setCustomAnimations(R.anim.zoom_slide_horizontal_right_in, R.anim.zoom_slide_horizontal_left_out,
                        R.anim.zoom_slide_horizontal_left_in, R.anim.zoom_slide_horizontal_right_out);
                break;
            case ZOOM_SLIDE_VERTICAL:
                ft.setCustomAnimations(R.anim.zoom_slide_vertical_right_in, R.anim.zoom_slide_vertical_left_out,
                        R.anim.zoom_slide_vertical_left_in, R.anim.zoom_slide_vertical_right_out);
                break;
        }
    }

    public static Animation createEmptyAnimation() {
        Animation noAnim = new Animation(){};
        noAnim.setDuration(0);
        return noAnim;
    }
}
