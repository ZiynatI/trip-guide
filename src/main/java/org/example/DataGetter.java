package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DataGetter {
    public void makeRequest() {
        String postData = "{\"direction\":[{\"depDate\":\"13.04.2023\",\"fullday\":true,\"type\":\"Forward\"},{\"depDate\":\"17.04.2023\",\"fullday\":true,\"type\":\"Backward\"}],\"stationFrom\":\"2900000\",\"stationTo\":\"2900700\",\"detailNumPlaces\":1,\"showWithoutPlaces\":0}";
        try {
            URL url = new URL("https://e-ticket.railway.uz/api/v1/trains/availability/space/between/stations");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setRequestProperty("Content-type", "application/json");
            con.setDoOutput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");

            OutputStream os = con.getOutputStream();
            os.write(postData.getBytes(StandardCharsets.UTF_8));
            os.close();

            InputStream in = new BufferedInputStream(con.getInputStream());
            String result = in.toString();
            System.out.println(result);
            in.close();
            con.disconnect();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
