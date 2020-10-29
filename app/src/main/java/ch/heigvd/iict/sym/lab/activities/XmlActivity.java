package ch.heigvd.iict.sym.lab.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import ch.heigvd.iict.sym.lab.Person;
import ch.heigvd.iict.sym.lab.Phone;
import ch.heigvd.iict.sym.lab.PhoneType;
import ch.heigvd.iict.sym.lab.R;
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener;
import ch.heigvd.iict.sym.lab.comm.Directory;
import ch.heigvd.iict.sym.lab.comm.HTTPMethod;
import ch.heigvd.iict.sym.lab.comm.SymComManager;
import ch.heigvd.iict.sym.lab.comm.SymComRequest;

public class XmlActivity extends AppCompatActivity {

    private final static String LOG_TAG = "XML_ACTIVITY";
    private final static String TAG_FROM_SERVER = "FROM_SERVER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml);

        final EditText etName = findViewById(R.id.xml_etName);
        final EditText etFirstName = findViewById(R.id.xml_etFirstName);
        final EditText etMiddleName = findViewById(R.id.xml_etMiddleName);
        final RadioGroup rgGender = findViewById(R.id.xml_rgGender);
        final EditText etPhoneNumber = findViewById(R.id.xml_etPhoneNumber);
        final Spinner spPhoneType = findViewById(R.id.xml_spPhoneType);
        final String serverURL = "http://sym.iict.ch/rest/xml";


        Button btnAdd = findViewById(R.id.xml_btnAddPerson);
        Button btnSend = findViewById(R.id.xml_btnSend);

        spPhoneType.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, PhoneType.values()));

        // Create the Handler to be able to modify the UIthread when receive specific message
        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.d(LOG_TAG, msg.getData().getString(TAG_FROM_SERVER));
                Toast.makeText(XmlActivity.this, getString(R.string.json_toast_send_successfully), Toast.LENGTH_LONG).show();
            }
        };

        final Directory directory = new Directory();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Phone phone = new Phone(etPhoneNumber.getText().toString(), (PhoneType) spPhoneType.getSelectedItem());

                int selectedGenderId = rgGender.getCheckedRadioButtonId();

                RadioButton rbSelectedGender = findViewById(selectedGenderId);

                // TODO User can add a list of phone
                Person person = new Person(etName.getText().toString(),
                        etFirstName.getText().toString(),
                        rbSelectedGender.getText().toString(),
                        new ArrayList<>(Collections.singletonList(phone)));

                if (etMiddleName.getText() != null) {
                    person.setMiddleName(etMiddleName.getText().toString());
                }

                directory.addPerson(person);

                etName.getText().clear();
                etFirstName.getText().clear();
                etMiddleName.getText().clear();
                rbSelectedGender.setChecked(false);
                etPhoneNumber.getText().clear();
                spPhoneType.setSelected(false);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                directory.xmlSerialize(),
                                HTTPMethod.POST,
                                "application/xml",
                                null)
                        );
                    }
                }).start();
            }


        });


        /*
        <?xml version="1.0" encoding="UTF-8"?>
        <!DOCTYPE directory SYSTEM "http://sym.iict.ch/directory.dtd">
        <directory>
            <person>
            <name>Doe</name>
            <firstname>Joe</firstname>
            <gender>M</gender>
            <phone type="home">321321</phone>
            </person>
        </directory>
         */


    }
}

