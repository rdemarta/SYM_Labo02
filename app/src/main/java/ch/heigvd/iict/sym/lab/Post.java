package ch.heigvd.iict.sym.lab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {

    private final static String TAG_TITLE = "title";
    private final static String TAG_DESCRIPTION = "description";
    private final static String TAG_DATE = "date";

    private String title;
    private String description;
    private Date date;

    public Post(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public static List<Post> parsePosts(JSONArray posts) throws JSONException, ParseException {
        List<Post> res = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");

        for(int i = 0; i< posts.length(); i++){
            JSONObject post = posts.getJSONObject(i);

            res.add(new Post(post.getString(TAG_TITLE),
                    post.getString(TAG_DESCRIPTION),
                    sdf.parse(post.getString(TAG_DATE))));
        }

        return res;
    }
}
