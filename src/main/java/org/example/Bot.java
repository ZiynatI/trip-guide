package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Bot {
    String token;
    List<String> params = new ArrayList<>();

    public Bot(String token) {
        this.token = token;
    }

    public void sendMessage(String title, String message, String chatId) throws IOException {
        System.out.println(URLEncoder.encode(message, StandardCharsets.UTF_8));
        URL url = new URL("https://api.telegram.org/bot" + token + "/sendMessage?chat_id=" + chatId + collectParams()
                + "&text=" + URLEncoder.encode("*" + correctReservedChars(title) + "*" + "\n" + correctReservedChars(message), StandardCharsets.UTF_8));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        InputStream is = con.getResponseCode() <= 299 ? con.getInputStream() : con.getErrorStream();
        String content;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            content = reader.lines().collect(Collectors.joining("\n"));
        }
        System.out.println(content);
    }

    public static String correctReservedChars(String message) {
        Character[] reserved = new Character[]{'!', '*', '\'', '(', ')', ';', ':', '@', '&', '=', '+', '$', ',', '/', '?', '%', '#', '[', ']' };
        for (char c : reserved) {
            message = message.replace(Character.toString(c), "\\" + c);
        }
        return message;
    }

    private String collectParams() {
        StringBuilder sb = new StringBuilder();
        for (String param : this.params) {
            sb.append("&").append(param);
        }
        return sb.toString();
    }

    public void setParam(String param) {
        params.add(param);
    }
}
