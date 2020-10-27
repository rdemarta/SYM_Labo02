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

        EditText etName = findViewById(R.id.json_etName);
        EditText editText = findViewById(R.id.json_etAge);
        Button btnSend = findViewById(R.id.main_btnJson);
    }
}