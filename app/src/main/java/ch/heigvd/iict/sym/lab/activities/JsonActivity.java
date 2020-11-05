package ch.heigvd.iict.sym.lab.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ch.heigvd.iict.sym.lab.R;
import ch.heigvd.iict.sym.lab.User;
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener;
import ch.heigvd.iict.sym.lab.comm.HTTPMethod;
import ch.heigvd.iict.sym.lab.comm.SymComManager;
import ch.heigvd.iict.sym.lab.comm.SymComRequest;

public class JsonActivity extends AppCompatActivity {

    private final static String LOG_TAG = "JSON_ACTIVITY";
    private final static String TAG_FROM_SERVER = "FROM_SERVER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        // Set the actionbar title
        getSupportActionBar().setTitle(R.string.json_actionbar_title);

        final EditText etLastName = findViewById(R.id.json_etLastName);
        final EditText etfirstName = findViewById(R.id.json_etFirstName);
        final EditText etAge = findViewById(R.id.json_etAge);
        final Button btnSend = findViewById(R.id.json_btnSend);
        final String serverURL = "http://sym.iict.ch/rest/json";

        // Create the Handler to be able to modify the UIthread when receive specific message
        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.d(LOG_TAG, msg.getData().getString(TAG_FROM_SERVER));
                Toast.makeText(JsonActivity.this, getString(R.string.json_toast_send_successfully), Toast.LENGTH_LONG).show();
            }
        };


        // Send data
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etLastName.getText().toString().isEmpty()) {
                    etLastName.setError(getString(R.string.json_cannot_be_empty));
                } else if(etfirstName.getText().toString().isEmpty()) {
                    etfirstName.setError(getString(R.string.json_cannot_be_empty));
                } else if(etAge.getText().toString().isEmpty()) {
                    etAge.setError(getString(R.string.json_cannot_be_empty));
                } else {
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
                                    new User(etfirstName.getText().toString(), etLastName.getText().toString(), Integer.parseInt(etAge.getText().toString())).serialize().toString(),
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