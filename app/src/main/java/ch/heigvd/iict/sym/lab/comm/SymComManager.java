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

    public void sendRequest(SymComRequest request) {
        try {
            // Init the connection
            URL url = new URL(request.getUrl());
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            // SetParams
            con.setRequestMethod(request.getHttpMethod().toString());
            con.setRequestProperty("Content-Type", request.getContentType());

            // We have no network speed explicitly selected => don't send header and the server will choose one randomly
            if(request.getNetworkSpeed() != null){
                con.setRequestProperty("X-Network:", request.getNetworkSpeed().toString());
            }

            // Set do output only for POST method
            if(request.getHttpMethod() == HTTPMethod.POST){
                con.setDoOutput(true);
            }

            // Write the output
            OutputStream os = con.getOutputStream();
            byte[] input = request.getMessage().getBytes(StandardCharsets.UTF_8);
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
