package ch.heigvd.iict.sym.lab.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.heigvd.iict.sym.lab.Author;
import ch.heigvd.iict.sym.lab.R;
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener;
import ch.heigvd.iict.sym.lab.comm.HTTPMethod;
import ch.heigvd.iict.sym.lab.comm.SymComManager;
import ch.heigvd.iict.sym.lab.comm.SymComRequest;

public class GraphQlActivity extends AppCompatActivity {

    private final static String TAG_FROM_SERVER_AUTHORS = "FROM_SERVER_AUTHORS";
    private final static String TAG_FROM_SERVER_POSTS = "FROM_SERVER_POSTS";
    private final static String LOG_TAG = "GRAPHQL_ACTIVITY";

    private final static String TAG_All_AUTHORS = "allAuthors";
    private final static String TAG_ID = "id";
    private final static String TAG_FIRST_NAME = "first_name";
    private final static String TAG_LAST_NAME = "last_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_ql);

        final Spinner spAuthors = findViewById(R.id.graphql_spAuthors);
        ScrollView svAllPosts = findViewById(R.id.graphql_svAllPosts);

        final String serverURL = "http://sym.iict.ch/api/graphql";

        final List<Author> authors = new ArrayList<>();

        final ArrayAdapter<Author> authorAdapter = new ArrayAdapter<Author>(GraphQlActivity.this, R.layout.support_simple_spinner_dropdown_item, authors) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                View view = super.getView(position, convertView, parent);
                Author item = getItem(position);
                ((TextView) view).setText(item.getFirstName() + " " + item.getLastName());
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                return getView(position, convertView, parent);
            }
        };


        // Get all authors
        // id to retrieve the corresponding posts
        //     "query": "{allAuthors{id first_name last_name}}"

        // Get all posts by id
        // "query": "{allPostByAuthor(authorId:1){title description content date}}"

        // 1. get All authors to populate the spinner
        // 2. retrieve the author id
        // 3. send the id to retrive the posts for this authors
        // Display => LiveData

        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.getData().containsKey(TAG_FROM_SERVER_AUTHORS)) {
                    try {
                        JSONObject object = new JSONObject(msg.getData().getString(TAG_FROM_SERVER_AUTHORS));

                        authors.addAll(parseAuthors(object.getJSONObject("data")));

                        spAuthors.setAdapter(authorAdapter);

                        authorAdapter.notifyDataSetChanged();

                        Toast.makeText(GraphQlActivity.this, "List des auteurs chargé !", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getData().get(TAG_FROM_SERVER_POSTS) != null) {
                    Log.d(LOG_TAG, msg.getData().getString(TAG_FROM_SERVER_POSTS));
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                SymComManager symComManager = new SymComManager();
                symComManager.setCommunicationEventListener(new CommunicationEventListener() {
                    @Override
                    public boolean handleServerResponse(String response) {
                        Message msg = handler.obtainMessage();
                        Bundle b = new Bundle();
                        b.putString(TAG_FROM_SERVER_AUTHORS, response);
                        msg.setData(b);
                        handler.sendMessage(msg);
                        return true;
                    }
                });

                String query = "{\"query\": \"{allAuthors{id first_name last_name}}\"}";
                Log.d(LOG_TAG, query);
                symComManager.sendRequest(new SymComRequest(
                        serverURL,
                        query,
                        HTTPMethod.POST,
                        "application/json",
                        null
                ));


            }
        }).start();
    }

    private List<Author> parseAuthors(JSONObject object) throws JSONException {
        JSONArray authors = object.getJSONArray(TAG_All_AUTHORS);
        List<Author> res = new ArrayList<>();
        for (int i = 0; i < authors.length(); i++) {
            JSONObject author = authors.getJSONObject(i);

            res.add(
                    new Author(author.getInt(TAG_ID),
                            author.getString(TAG_FIRST_NAME),
                            author.getString(TAG_LAST_NAME)));
        }
        return res;
    }
}

