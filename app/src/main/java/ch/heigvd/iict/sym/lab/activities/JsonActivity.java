package ch.heigvd.iict.sym.lab.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import ch.heigvd.iict.sym.lab.R;
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener;
import ch.heigvd.iict.sym.lab.comm.SymComManager;

public class JsonActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        final EditText etName = findViewById(R.id.json_etName);
        final EditText etAge = findViewById(R.id.json_etAge);
        Button btnSend = findViewById(R.id.json_btnSend);
        final ListView lvResponse = findViewById(R.id.json_lvResponse);

        final ArrayList<String> responseList = new ArrayList();

        lvResponse.setAdapter(new ArrayAdapter<String>(this, R.layout.listitem, responseList));


        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle b = msg.getData();
                Set<String> keys = b.keySet();

                for (String key : keys) {
                    // TODO Need to deserialize infos
                    responseList.add(key + " : " + b.getString(key));
                }

                ((BaseAdapter) lvResponse.getAdapter()).notifyDataSetChanged();

            }
        };

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty()) {
                    etName.setError("Name cannot be empty");
                } else if (etAge.getText().toString().isEmpty()) {
                    etAge.setError("Age canot be empty");
                } else {
                    final JSONObject payload = new JSONObject();

                    try {
                        payload.put("name", etName.getText().toString());
                        payload.put("age", etAge.getText().toString());

                        etName.getText().clear();
                        etAge.getText().clear();

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SymComManager scm = new SymComManager();
                                scm.setCommunicationEventListener(new CommunicationEventListener() {
                                    @Override
                                    public boolean handleServerResponse(String response) {
                                        Message msg = handler.obtainMessage();
                                        Bundle b = new Bundle();

                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            Iterator<String> it = jsonResponse.keys();
                                            while (it.hasNext()) {
                                                String key = (String) it.next();
                                                String value = jsonResponse.getString(key);
                                                b.putString(key, value);
                                            }
                                            msg.setData(b);
                                            handler.sendMessage(msg);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        return true;
                                    }
                                });

                                scm.sendRequest("http://sym.iict.ch/rest/json", payload.toString());
                            }
                        });

                        thread.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }
}