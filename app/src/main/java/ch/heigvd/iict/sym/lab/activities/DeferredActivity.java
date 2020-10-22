package ch.heigvd.iict.sym.lab.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import ch.heigvd.iict.sym.lab.R;
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener;
import ch.heigvd.iict.sym.lab.comm.SymComManager;
import ch.heigvd.iict.sym.lab.utils.Utils;

public class DeferredActivity extends AppCompatActivity {

    private static final String TAG ="DeferredActivity";
    private TextView tvDataReceive;


    private static class MyHandler extends Handler {
        private final WeakReference<DeferredActivity> mActivity;

        public MyHandler(DeferredActivity mActivity) {
            super(Looper.myLooper());
            this.mActivity = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            DeferredActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleCallBack(msg);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deferred);

        tvDataReceive = findViewById(R.id.deferred_tvDataReceived);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final SymComManager scm = new SymComManager();
                scm.setCommunicationEventListener(new CommunicationEventListener() {
                    @Override
                    public boolean handleServerResponse(String response) {
                        return true;
                    }
                });
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(Utils.isConnected(DeferredActivity.this.getApplicationContext())) {
                            Log.d(TAG, "Connected !");
                            scm.sendRequest("http://sym.iict.ch/rest/txt", "Hey !");
                            timer.cancel();
                            timer.purge();
                        }
                    }
                }, 0, 1000);
            }
        });

        thread.start();
    }

    private void handleCallBack(Message msg){
        tvDataReceive.setText(msg.getData().getString("FROM_SERVER"));
    }

}