package ch.heigvd.iict.sym.lab.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ch.heigvd.iict.sym.lab.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAsynchronous = findViewById(R.id.main_btnAsynchronous);
        btnAsynchronous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AsynchronousActivity.class);
                startActivity(intent);
            }
        });

        Button btnDiffered = findViewById(R.id.main_btnDiffered);
        btnDiffered.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, DeferredActivity.class);
               startActivity(intent);
           }
       }
        );
    }
}