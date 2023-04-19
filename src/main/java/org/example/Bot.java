package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Bot {
    private final String token;
    private Map<String, String> params;
    private int lastUpdateId;

    public Bot(String token) {
        this.token = token;
        this.params = new HashMap<>();
        params.put("parse_mode", "MarkdownV2");
    }
public void sendMessageToItself() throws IOException{
    URL url = new URL("https://api.telegram.org/bot" + token + "/sendMessage?chat_id=" + token + collectParamsToString()
            + "&text=" + URLEncoder.encode("*" + escapeReservedChars("hi") + "*" + "\n" + escapeReservedChars("message"), StandardCharsets.UTF_8));
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    InputStream is = con.getResponseCode() <= 299 ? con.getInputStream() : con.getErrorStream();
    String content;
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
        content = reader.lines().collect(Collectors.joining("\n"));
        Map response = parse(content);
        System.out.println(response);
        if (response.get("ok").equals("false")) {
            throw new IOException("error_code:" + response.get("error_code") + response.get("description"));
        }
    }
}

    public String getBotMessage() throws IOException {
        updateLastUpdateId();
        int nextUpdateId = lastUpdateId + 1;
        String messageText;
        JsonNode jsonResponse;
        while (true) {
            URL url = new URL("https://api.telegram.org/bot" + token + "/getUpdates?offset=-100" + nextUpdateId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            ObjectMapper mapper = new ObjectMapper();
            try (InputStream is = con.getResponseCode() <= 299 ? con.getInputStream() : con.getErrorStream()) {
                jsonResponse = mapper.readTree(is);
                if (jsonResponse.get("ok").asText().equals("false")) {
                    throw new IOException("error_code:" + jsonResponse.get("error_code") + jsonResponse.get("description"));
                }
                if (jsonResponse.get("result").get(0) != null) {
                    messageText = jsonResponse.get("result").get(0).get("message").get("text").asText();
                    break;
                }
            }
        }
        return messageText;
    }

    public void sendMessage(String title, String message, String chatId) throws IOException {
        URL url = new URL("https://api.telegram.org/bot" + token + "/sendMessage?chat_id=" + chatId + collectParamsToString()
                + "&text=" + URLEncoder.encode("*" + escapeReservedChars(title) + "*" + "\n" + escapeReservedChars(message), StandardCharsets.UTF_8));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        InputStream is = con.getResponseCode() <= 299 ? con.getInputStream() : con.getErrorStream();
        String content;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            content = reader.lines().collect(Collectors.joining("\n"));
            Map response = parse(content);
            System.out.println(response);
            if (response.get("ok").equals("false")) {
                throw new IOException("error_code:" + response.get("error_code") + response.get("description"));
            }
        }
    }

    private static String escapeReservedChars(String message) {
        Character[] reserved = new Character[]{'!', '*', '\'', '(', ')', ';', ':', '@', '&', '=', '+', '$', ',', '/', '?', '%', '#', '[', ']', '.'};
        for (char c : reserved) {
            message = message.replace(Character.toString(c), "\\" + c);
        }
        return message;
    }

    private String collectParamsToString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> param : this.params.entrySet()) {
            sb.append("&").append(param.getKey()).append("=").append(param.getValue());
        }
        return sb.toString();
    }

    private Map parse(String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap map = objectMapper.readValue(content, HashMap.class);
        return map;
    }

    public void updateLastUpdateId() throws IOException {
        URL url = new URL("https://api.telegram.org/bot" + token + "/getUpdates?offset=-1");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        JsonNode jsonNodeResponse;
        try (InputStream is = con.getResponseCode() <= 299 ? con.getInputStream() : con.getErrorStream()) {
            ObjectMapper mapper = new ObjectMapper();
            jsonNodeResponse = mapper.readTree(is);
        }
        JsonNode result = jsonNodeResponse.get("result");
        JsonNode result2 = result.get(0);
        lastUpdateId = jsonNodeResponse.get("result").get(0).get("update_id").asInt();
    }

    public int getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(int lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

}
