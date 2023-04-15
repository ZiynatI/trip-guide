package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static List<Train> getTrainsFromString(String text) {
        List<Train> list = new ArrayList<>();
        String copy = new String(text);
        while (copy.contains("\"length\"")) {
            Train train = new Train();
            copy = copy.substring(copy.indexOf("\"length\""));
            String s = copy.substring(10, copy.indexOf(',') - 1);
            train.setLength(s);

            copy = copy.substring(copy.indexOf("\"type\""));
            s = copy.substring(8, copy.indexOf(',') - 1);
            train.setType(s);

            copy = copy.substring(copy.indexOf("\"number\""));
            s = copy.substring(10, copy.indexOf(',') - 1);
            train.setNumber(s);

            copy = copy.substring(copy.indexOf("\"brand\""));
            s = copy.substring(9, copy.indexOf(',') - 1);
            train.setBrand(s);

            list.add(train);
        }
        return list;
    }
}
