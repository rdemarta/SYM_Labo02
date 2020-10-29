package ch.heigvd.iict.sym.lab.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import ch.heigvd.iict.sym.lab.R;

public class JsonActivity extends AppCompatActivity {

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
    }
}