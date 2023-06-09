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
    String depDate;
    int stationFrom;
    int stationTo;

    public DataGetter(String depDate, int stationFrom, int stationTo) {
        this.depDate = depDate;
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
        System.out.println("dg active");
    }

    public JsonNode makeRequestGetResponse() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse;
        Map jsonRequest = makeJsonRequestMap();
        URL url = new URL("https://e-ticket.railway.uz/api/v1/trains/availability/space/between/stations");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/112.0");
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

    public List<Train> jsonToTrainsList(JsonNode jsonMap) {
        JsonNode response = jsonMap.get("express").get("direction").get(0).get("trains").get(0).get("train");
        List<Train> trainsList = new ArrayList<>();

        for (int i = 0; i < response.size(); i++) {
            JsonNode trainJson = response.get(i);

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

    private Map makeJsonRequestMap() {
        Map jsonRequest = new HashMap<>();
        Map direction = new HashMap<>();
        direction.put("depDate", depDate);
        direction.put("fullday", true);
        direction.put("type", "Forward");
        Object[] directionArr = new Object[]{direction};
        jsonRequest.put("direction", directionArr);
        jsonRequest.put("stationFrom", stationFrom);
        jsonRequest.put("stationTo", stationTo);
        jsonRequest.put("detailNumPlaces", 1);
        jsonRequest.put("showWithoutPlaces", 0);
        return jsonRequest;
    }
}
