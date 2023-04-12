package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class DataGetter {
    public void makeRequest() {
        String postData = "{\"direction\":[{\"depDate\":\"13.04.2023\",\"fullday\":true,\"type\":\"Forward\"},{\"depDate\":\"17.04.2023\",\"fullday\":true,\"type\":\"Backward\"}],\"stationFrom\":\"2900000\",\"stationTo\":\"2900700\",\"detailNumPlaces\":1,\"showWithoutPlaces\":0}";
        try {
            URL url = new URL("https://e-ticket.railway.uz/api/v1/trains/availability/space/between/stations");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setRequestProperty("Content-type", "application/json");
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            con.setRequestProperty("Origin", "https://e-ticket.railway.uz");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Referer", "https://e-ticket.railway.uz/en/home");
            con.setRequestProperty("Sec-Fetch-Dest", "empty");
            con.setRequestProperty("Sec-Fetch-Mode", "cors");
            con.setRequestProperty("Sec-Fetch-Site", "same-origin");

            OutputStream os = con.getOutputStream();
            os.write(postData.getBytes(StandardCharsets.UTF_8));
            os.close();

            InputStream in = new BufferedInputStream(con.getInputStream());
            String text = new BufferedReader(
                    new InputStreamReader(in, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            System.out.println(text);
            in.close();
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
