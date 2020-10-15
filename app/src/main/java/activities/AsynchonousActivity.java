package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.heigvd.iict.sym.lab.R;
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener;
import ch.heigvd.iict.sym.lab.comm.SymComManager;

public class AsynchonousActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynchonous);

        // Set the actionbar title
        getSupportActionBar().setTitle(R.string.asynchrone_actionbar_title);

        final RelativeLayout backgroundLoader = findViewById(R.id.asynchronous_layoutLoader);
        final ProgressBar loader =  findViewById(R.id.asynchronous_loader);
        final TextView tvDataReceived = findViewById(R.id.asynchronous_tvDataReceived);

        // Create the Handler to be able to modify the UIthread when receive specific message
        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                hideLoader(backgroundLoader, loader);
                tvDataReceived.setText(msg.getData().getString("FROM_SERVER"));
                Log.d("AsynchonousActivity", msg.getData().getString("FROM_SERVER"));
            }
        };

        // Our thread to send the request, fetch the response and send it through the handler asynchronously
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                displayLoader(backgroundLoader, loader);

                SymComManager symComManager = new SymComManager();
                symComManager.setCommunicationEventListener(new CommunicationEventListener() {
                    @Override
                    public boolean handleServerResponse(String response) {
                        Message msg = handler.obtainMessage();
                        Bundle b = new Bundle();
                        b.putString("FROM_SERVER", response);
                        msg.setData(b);
                        handler.sendMessage(msg);
                        return true;
                    }
                });

                symComManager.sendRequest("http://sym.iict.ch/rest/txt", "Hey !");
            }
        });

        thread.start();

    }

    /**
     * Display the loader
     * @param backgroundLoader RelativeLayout background
     * @param loader ProgressBar loader
     */
    private void displayLoader(RelativeLayout backgroundLoader, ProgressBar loader) {
        backgroundLoader.setVisibility(View.VISIBLE);
        loader.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the loader
     * @param backgroundLoader RelativeLayout background
     * @param loader ProgressBar loader
     */
    private void hideLoader(RelativeLayout backgroundLoader, ProgressBar loader) {
        loader.setVisibility(View.GONE);
        backgroundLoader.setVisibility(View.GONE);
    }
}