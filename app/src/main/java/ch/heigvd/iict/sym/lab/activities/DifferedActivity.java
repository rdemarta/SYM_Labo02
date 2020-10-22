package ch.heigvd.iict.sym.lab.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import ch.heigvd.iict.sym.lab.R;

public class DifferedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differed);

        TextView tvDataReceive = findViewById(R.id.differed_tvDataReceived);
    }
}