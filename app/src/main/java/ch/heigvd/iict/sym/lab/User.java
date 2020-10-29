package ch.heigvd.iict.sym.lab;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String firstName;
    private String lastName;
    private int age;

    public User(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public JSONObject serialize() {
        try {
            return new JSONObject()
                    .put("firstName", firstName)
                    .put("lastName", lastName)
                    .put("age", age);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
