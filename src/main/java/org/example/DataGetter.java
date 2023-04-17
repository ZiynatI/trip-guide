package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataGetter {
    public JsonNode makeRequestGetResponse() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse;
        Map jsonRequest = collectJsonRequest();
        URL url = new URL("https://e-ticket.railway.uz/api/v1/trains/availability/space/between/stations");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            con.setRequestProperty("Content-type", "application/json");
            try (OutputStream os = con.getOutputStream()) {
                os.write(mapper.writeValueAsString(jsonRequest).getBytes());
                try (InputStream in = new BufferedInputStream(con.getInputStream())) {
                    jsonResponse = mapper.readTree(in);

                }
            }
        } finally {
            con.disconnect();
        }
        return jsonResponse;
    }

    public static List<Train> jsonToTrainsList(JsonNode jsonMap) {
        JsonNode express = jsonMap.get("express").get("direction").get(0).get("trains").get(0).get("train");
        List<Train> trainsList = new ArrayList<>();

        for (int i = 0; i < express.size(); i++) {
            JsonNode trainJson = express.get(i);

            JsonNode placesJson = trainJson.get("places").get("cars");
            List<Place> places = new ArrayList<>();
            for (int j = 0; j < placesJson.size(); j++) {
                JsonNode carJson = placesJson.get(j);
                Place place = new Place(carJson.get("type").asText(),
                        carJson.get("tariffs").get("tariff").get(0).get("tariff").asInt() +
                                carJson.get("tariffs").get("tariff").get(0).get("comissionFee").asInt(),
                        carJson.get("freeSeats").asInt());
                places.add(place);
            }

            Train train = new Train(trainJson.get("length").asInt(),
                    trainJson.get("type").asText(),
                    trainJson.get("number").asText(),
                    trainJson.get("brand").asText(),
                    trainJson.get("route").get("station").get(0).asText(),
                    trainJson.get("route").get("station").get(1).asText(),
                    places,
                    trainJson.get("timeInWay").asText(),
                    trainJson.get("departure").get("localTime").asText(),
                    trainJson.get("arrival").get("localTime").asText());

            trainsList.add(train);
        }
        return trainsList;
    }

    public static Map collectJsonRequest() {
        Map jsonRequest = new HashMap<>();
        Map direction = new HashMap<>();
        direction.put("depDate", "13.05.2023");
        direction.put("fullday", true);
        direction.put("type", "Forward");
        Object[] directionArr = new Object[]{direction};
        jsonRequest.put("direction", directionArr);
        jsonRequest.put("stationFrom", "2900000");
        jsonRequest.put("stationTo", "2900700");
        jsonRequest.put("detailNumPlaces", 1);
        jsonRequest.put("showWithoutPlaces", 0);
        return jsonRequest;
    }
}
