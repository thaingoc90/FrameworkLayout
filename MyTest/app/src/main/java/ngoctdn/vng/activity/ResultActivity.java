package ngoctdn.vng.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ngoctdn.vng.utils.Log;

public class ResultActivity extends AppCompatActivity {

    public static final String ACTION_FINISH_QUICK_ACTIVITY = "ngoctdn.vng.FinishResultActivity";
    private BroadcastReceiver myBR = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ResultActivity", "Ngoctdn: onCreate");
        setContentView(R.layout.activity_result);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_FINISH_QUICK_ACTIVITY);
        myBR = new MyBroadcastReceiver();
        registerReceiver(myBR, intentFilter);
    }

    @Override
    protected void onDestroy() {
        Log.d("ResultActivity", "Ngoctdn: onDestroy");
        unregisterReceiver(myBR);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.d("ResultActivity", "Ngoctdn: onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d("ResultActivity", "Ngoctdn: onStop");
        super.onStop();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("ResultActivity", "Ngoctdn: onReceive1");
            if (intent != null) {
                Log.d("ResultActivity", "Ngoctdn: onReceive");
                finish();
            }
        }
    }
}
