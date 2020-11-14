package ch.heigvd.iict.sym.lab.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ch.heigvd.iict.sym.lab.R;
import ch.heigvd.iict.sym.lab.Utils;
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener;
import ch.heigvd.iict.sym.lab.comm.HTTPMethod;
import ch.heigvd.iict.sym.lab.comm.SymComManager;
import ch.heigvd.iict.sym.lab.comm.SymComRequest;

public class DelayedActivity extends AppCompatActivity {

    private final static String LOG_TAG = "DELAYED_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delayed);

        // Set the actionbar title
        getSupportActionBar().setTitle(R.string.delayed_actionbar_title);

        final EditText etTextToSend = findViewById(R.id.delayed_etTextToSend);
        final Button btnSend = findViewById(R.id.delayed_btnSend);
        final String serverURL = "http://sym.iict.ch/rest/txt";

        // Create the Thread object that will check internet connection and if there is connection
        // send all data stored in the Utils waiting list
        final Thread threadSendData = new Thread(new Runnable() {
            private Context context = DelayedActivity.this;
            @Override
            public void run() {
                while (true) {
                    Log.d(LOG_TAG, "Check internet connection");
                    // Internet OK
                    if (Utils.isConnectedToInternet(context)) {
                        Log.d(LOG_TAG, "Internet connection OK");
                        SymComManager symComManager = new SymComManager();
                        symComManager.setCommunicationEventListener(new CommunicationEventListener() {
                            @Override
                            public boolean handleServerResponse(String response) {
                                Log.d(LOG_TAG, response);
                                return true;
                            }
                        });

                        // Send all msg stored in the wainting list
                        for(String valueToSend : Utils.valuesWaitingList) {
                            Log.d(LOG_TAG, "Send data \"" + valueToSend + "\"");
                            symComManager.sendRequest(new SymComRequest(
                                    serverURL,
                                    valueToSend,
                                    HTTPMethod.POST,
                                    "text/plain",
                                    false,
                                    null)
                            );
                        }

                        // Clear the list and close the thread
                        Utils.valuesWaitingList.clear();

                        break;
                    }

                    // Wait 2sec to not spam a lot
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Btn send click
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Edit text must have a value
                if(etTextToSend.getText().toString().isEmpty()){
                    etTextToSend.setError(getString(R.string.asynchonous_cannot_be_empty));
                }else{

                    // No internet -> notify user
                    if (!Utils.isConnectedToInternet(DelayedActivity.this)) {
                        Toast.makeText(DelayedActivity.this, "Aucune connexion internet, l'envoi va être effectué des que la connexion sera rétablie", Toast.LENGTH_LONG).show();
                    }

                    // Add new values to the waiting list
                    Log.d(LOG_TAG, "Add \"" + etTextToSend.getText().toString() + "\" to the waiting list");
                    Utils.valuesWaitingList.add(etTextToSend.getText().toString());

                    // Run the thread only if is not currently running
                    if(!threadSendData.isAlive()){
                        Log.d(LOG_TAG, "Run thread");
                        threadSendData.start();
                    }

                }
            }

        });

    }

}