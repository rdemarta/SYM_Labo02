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
import java.util.List;

import ch.heigvd.iict.sym.lab.Person;
import ch.heigvd.iict.sym.lab.Phone;
import ch.heigvd.iict.sym.lab.PhoneType;
import ch.heigvd.iict.sym.lab.R;
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener;
import ch.heigvd.iict.sym.lab.Directory;
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

        // Set the actionbar title
        getSupportActionBar().setTitle(R.string.xml_action_title);

        final EditText etName = findViewById(R.id.xml_etName);
        final EditText etFirstName = findViewById(R.id.xml_etFirstName);
        final EditText etMiddleName = findViewById(R.id.xml_etMiddleName);
        final RadioGroup rgGender = findViewById(R.id.xml_rgGender);
        final EditText etPhoneNumber = findViewById(R.id.xml_etPhoneNumber);
        final Spinner spPhoneType = findViewById(R.id.xml_spPhoneType);
        final EditText etPhoneNumberOptional = findViewById(R.id.xml_etPhoneNumberOptional);
        final Spinner spPhoneTypeOptional = findViewById(R.id.xml_spPhoneTypeOptional);
        final String serverURL = "http://sym.iict.ch/rest/xml";


        Button btnAdd = findViewById(R.id.xml_btnAddPerson);
        Button btnSend = findViewById(R.id.xml_btnSend);

        spPhoneType.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, PhoneType.values()));
        spPhoneTypeOptional.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, PhoneType.values()));

        // Create the Handler to be able to modify the UIthread when receive specific message
        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.d(LOG_TAG, msg.getData().getString(TAG_FROM_SERVER));
                Toast.makeText(XmlActivity.this, getString(R.string.json_toast_send_successfully), Toast.LENGTH_LONG).show();
            }
        };

        final Directory directory = new Directory();

        // Add a new person in the directory
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton lastRb = findViewById(R.id.xml_rbMale);

                // Clean error
                lastRb.setError(null);


                // Check input
                boolean inputError = false;

                if (etName.getText().toString().trim().isEmpty()) {
                    inputError = true;
                    etName.setError(getString(R.string.xml_mandatoryField));
                }

                if (etFirstName.getText().toString().trim().isEmpty()) {
                    inputError = true;
                    etFirstName.setError(getString(R.string.xml_mandatoryField));
                }

                // No option selected
                if (rgGender.getCheckedRadioButtonId() == -1) {
                    inputError = true;
                    lastRb.setError(getString(R.string.xml_mandatoryField));
                }

                if (etPhoneNumber.getText().toString().trim().isEmpty()) {
                    inputError = true;
                    etPhoneNumber.setError(getString(R.string.xml_mandatoryField));
                }

                // Exit if at least one mandatory field is missing
                if (inputError) {
                    return;
                }

                List<Phone> phones = new ArrayList<>();

                Phone phone = new Phone(etPhoneNumber.getText().toString(), (PhoneType) spPhoneType.getSelectedItem());

                phones.add(phone);

                // TODO change into a recycle view to reuse the inputs for the phone number
                // With that user can add more than 2 phone numbers
                if (!etPhoneNumberOptional.getText().toString().isEmpty()) {
                    Phone optionalPhone = new Phone(etPhoneNumberOptional.getText().toString(), (PhoneType) spPhoneTypeOptional.getSelectedItem());
                    phones.add(optionalPhone);
                }

                int selectedGenderId = rgGender.getCheckedRadioButtonId();

                RadioButton rbSelectedGender = findViewById(selectedGenderId);
                Person person = new Person(etName.getText().toString(),
                        etFirstName.getText().toString(),
                        rbSelectedGender.getText().toString(),
                        phones);

                // Add middle name only if user has one
                if (!etMiddleName.getText().toString().isEmpty()) {
                    person.setMiddleName(etMiddleName.getText().toString());
                }

                directory.addPerson(person);

                // Clean all inputs
                etName.getText().clear();
                etFirstName.getText().clear();
                etMiddleName.getText().clear();
                etPhoneNumber.getText().clear();
                spPhoneType.setSelected(false);
                etPhoneNumberOptional.getText().clear();
                spPhoneTypeOptional.setSelected(false);
                rgGender.clearCheck();

                Toast.makeText(XmlActivity.this, R.string.xml_toast_add_new_person, Toast.LENGTH_LONG).show();

            }
        });

        // Send data
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!directory.isEmpty()) {

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
                                    directory.xmlSerialize(),
                                    HTTPMethod.POST,
                                    "application/xml",
                                    false,
                                    null)
                            );
                        }
                    }).start();
                } else {
                    Toast.makeText(XmlActivity.this, R.string.xml_toast_empty_directory, Toast.LENGTH_LONG).show();

                }

            }

        });


    }
}

