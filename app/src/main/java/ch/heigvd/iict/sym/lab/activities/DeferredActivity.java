package ch.heigvd.iict.sym.lab.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import ch.heigvd.iict.sym.lab.R;
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener;
import ch.heigvd.iict.sym.lab.comm.SymComManager;
import ch.heigvd.iict.sym.lab.utils.Utils;

public class DeferredActivity extends AppCompatActivity {

    private static final String TAG = "DeferredActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deferred);


        final EditText etDataInput = findViewById(R.id.deferred_etDataInput);

        Button btnSend = findViewById(R.id.deferred_btnSend);

        final ListView lvData = findViewById(R.id.deferred_dataList);

        final LinkedList<String> msgList = new LinkedList<>();


        lvData.setAdapter(new ArrayAdapter<String>(this, R.layout.listitem, msgList));

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etDataInput.getText().toString().trim().isEmpty()) {
                    msgList.push(etDataInput.getText().toString().trim());
                    ((BaseAdapter) lvData.getAdapter()).notifyDataSetChanged();
                    etDataInput.getText().clear();
                } else {
                    etDataInput.setError("Nothing to send");
                }
            }
        });

        // Thread use to send data when the user has a connection
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
                        if (!msgList.isEmpty() && Utils.isConnected(DeferredActivity.this.getApplicationContext())) {
                            Log.d(TAG, "Connected !");
                            String msg = msgList.pop();
                            scm.sendRequest("http://sym.iict.ch/rest/txt", msg);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((BaseAdapter) lvData.getAdapter()).notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }, 0, 1000);
            }
        });

        thread.start();
    }

}