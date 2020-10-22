package ch.heigvd.iict.sym.lab.comm;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SymComManager {

    private CommunicationEventListener communicationEventListener = null;
    private final static String LOG_TAG = "SYM_COM";

    public void sendRequest(String strURL, String message) {
        try {
            // Init the connection
            URL url = new URL (strURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            // SetParams
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/plain; utf-8");
            con.setRequestProperty("X-Network:", "CSD"); // For the moment hardcode the bad quality
            con.setDoOutput(true);

            // Write the output
            OutputStream os = con.getOutputStream();
            byte[] input = message.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

            // Read the response
            InputStreamReader in = new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(in);
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim() + "\n");
            }

            Log.d(LOG_TAG, response.toString());

            // Callback call to notify the response
            communicationEventListener.handleServerResponse(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setCommunicationEventListener(CommunicationEventListener communicationEventListener) {
        this.communicationEventListener = communicationEventListener;
    }
	
}
