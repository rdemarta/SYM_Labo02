package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import ch.heigvd.iict.sym.lab.R;

public class DelayedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delayed);

        // Set the actionbar title
        getSupportActionBar().setTitle(R.string.delayed_actionbar_title);

        final EditText etTextToSend = findViewById(R.id.delayed_etTextToSend);
        final Button btnSend = findViewById(R.id.delayed_btnSend);
        final String serverURL = "http://sym.iict.ch/rest/txt";

    }
}