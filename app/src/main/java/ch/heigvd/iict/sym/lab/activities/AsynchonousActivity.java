package ch.heigvd.iict.sym.lab.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ch.heigvd.iict.sym.lab.R;
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener;
import ch.heigvd.iict.sym.lab.comm.HTTPMethod;
import ch.heigvd.iict.sym.lab.comm.SymComManager;
import ch.heigvd.iict.sym.lab.comm.SymComRequest;

public class AsynchonousActivity extends AppCompatActivity {

    private final static String TAG_FROM_SERVER = "FROM_SERVER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynchonous);

        // Set the actionbar title
        getSupportActionBar().setTitle(R.string.asynchrone_actionbar_title);

        final TextView tvDataReceived = findViewById(R.id.asynchronous_tvDataReceived);
        final EditText etTextToSend = findViewById(R.id.asynchronous_etTextToSend);
        final Button btnSend = findViewById(R.id.asynchronous_btnSend);
        final String serverURL = "http://sym.iict.ch/rest/txt";

        // Create the Handler to be able to modify the UIthread when receive specific message
        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                tvDataReceived.setText(msg.getData().getString(TAG_FROM_SERVER));
            }
        };


        // Send data
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etTextToSend.getText().toString().isEmpty()){
                    etTextToSend.setError(getString(R.string.asynchonous_cannot_be_empty));
                }else{
                    // Recreate each time a new Thread to be able to start each time a new thread (impossible to re-run the same thread)
                    // Our thread to send the request, fetch the response and send it through the handler asynchronously
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SymComManager symComManager = new SymComManager();
                            symComManager.setCommunicationEventListener(new CommunicationEventListener() {
                                @Override
                                public boolean handleServerResponse(String response) {
                                    Message msg = handler.obtainMessage();
                                    Bundle b = new Bundle();
                                    b.putString(TAG_FROM_SERVER, response);
                                    msg.setData(b);
                                    handler.sendMessage(msg);
                                    return true;
                                }
                            });

                            symComManager.sendRequest(new SymComRequest(
                                    serverURL,
                                    etTextToSend.getText().toString(),
                                    HTTPMethod.POST,
                                    "test/plain",
                                    false,
                                    null)
                            );
                        }
                    }).start();
                }
            }
        });

    }
}