// Authors: Loïc Dessaules, Robin Demarta, Chau Ying Kot

package ch.heigvd.iict.sym.lab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Author {

    private final static String TAG_ID = "id";
    private final static String TAG_FIRST_NAME = "first_name";
    private final static String TAG_LAST_NAME = "last_name";

    private int id;
    private String firstName;
    private String lastName;

    public Author(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getId() {
        return id;
    }

    /**
     * Transform a json array into a list of authors
     *
     * @param authors a json array contains authors data
     * @return a list of authors
     * @throws JSONException
     */
    public static List<Author> parseAuthors(JSONArray authors) throws JSONException {
        List<Author> res = new ArrayList<>();
        for (int i = 0; i < authors.length(); i++) {
            JSONObject author = authors.getJSONObject(i);

            res.add(new Author(author.getInt(TAG_ID),
                    author.getString(TAG_FIRST_NAME),
                    author.getString(TAG_LAST_NAME)));
        }
        return res;
    }
}
