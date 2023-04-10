package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Bot {
    String token;
    Map<String, String> params;

    public Bot(String token) {
        this.token = token;
        this.params = new HashMap<>();
        params.put("parse_mode", "MarkdownV2");
    }

    public static void sendRequest() throws MalformedURLException {
        URL url = new URL("https://e-ticket.railway.uz/api/v1/trains/availability/space/between/stations");
        String request = "POST /api/v1/trains/availability/space/between/stations HTTP/1.1\n" +
                "Host: e-ticket.railway.uz\n\n";
    }

    public int getCityNum() throws IOException {
        URL url = new URL("https://api.telegram.org/bot" + token + "/getUpdates?offset=-1");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        InputStream is = con.getResponseCode() <= 299 ? con.getInputStream() : con.getErrorStream();
        String content;
        Map response;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            content = reader.lines().collect(Collectors.joining("\n"));
            response = parse(content);
            System.out.println(content);
            if (response.get("ok").equals("false")) {
                throw new IOException("error_code:" + response.get("error_code") + response.get("description"));
            }
        }
        return Integer.parseInt(content.substring(content.lastIndexOf(':') + 2, content.lastIndexOf('\"')));
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

    public static String escapeReservedChars(String message) {
        Character[] reserved = new Character[]{'!', '*', '\'', '(', ')', ';', ':', '@', '&', '=', '+', '$', ',', '/', '?', '%', '#', '[', ']', '.' };
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

    public Map parse(String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap map = objectMapper.readValue(content, HashMap.class);
        return map;
    }
}
