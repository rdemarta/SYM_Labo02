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
        Button btnDelayed = findViewById(R.id.main_btnDelayed);
        Button btnJson = findViewById(R.id.main_btnJson);
        Button btnXml = findViewById(R.id.main_btnXml);
        Button compressedBtn = findViewById(R.id.main_btnCompressed);

        btnAsynchronous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AsynchonousActivity.class);
                startActivity(intent);
            }
        });

        btnDelayed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DelayedActivity.class);
                startActivity(intent);
            }
        });

        btnJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JsonActivity.class);
                startActivity(intent);
            }
        });

        btnXml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, XmlActivity.class);
                startActivity(intent);
            }
        });

        compressedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CompressedActivity.class);
                startActivity(intent);
            }
        });
    }
}