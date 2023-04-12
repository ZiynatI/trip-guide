package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.stream.Collectors;

public class DataGetter {
    public void makeRequest() throws IOException {
        String postData = "{\"direction\":[{\"depDate\":\"13.04.2023\",\"fullday\":true,\"type\":\"Forward\"},{\"depDate\":\"17.04.2023\",\"fullday\":true,\"type\":\"Backward\"}],\"stationFrom\":\"2900000\",\"stationTo\":\"2900700\",\"detailNumPlaces\":1,\"showWithoutPlaces\":0}";
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> jsonmap = mapper.readValue(postData, HashMap.class);
        URL url = new URL("https://e-ticket.railway.uz/api/v1/trains/availability/space/between/stations");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            con.setRequestProperty("Content-type", "application/json");
            try (OutputStream os = con.getOutputStream()) {
                os.write(mapper.writeValueAsString(jsonmap).getBytes());
                try (InputStream in = new BufferedInputStream(con.getInputStream())) {
                    String text = new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    System.out.println(text);
                }
            }
        } finally {
            con.disconnect();
        }
    }
}
