package ch.heigvd.iict.sym.lab.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import ch.heigvd.iict.sym.lab.R;

public class XmlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml);

        EditText etName = findViewById(R.id.xml_etName);
        EditText etFirstName = findViewById(R.id.xml_etFirstName);
        EditText etMiddleName = findViewById(R.id.xml_etMiddleName);
        RadioGroup rgGender = findViewById(R.id.xml_rgGender);
        EditText etPhoneNumber = findViewById(R.id.xml_etPhoneNumber);
        Spinner spPhoneType = findViewById(R.id.xml_spPhoneType);

        ListView lvResponse = findViewById(R.id.xml_lvResponse);

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