package ch.heigvd.iict.sym.lab.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.Spinner;

import ch.heigvd.iict.sym.lab.R;

public class GraphQlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_ql);

        Spinner spAuthors = findViewById(R.id.graphql_spAuthors);
        ScrollView svAllPosts = findViewById(R.id.graphql_svAllPosts);



    }
}