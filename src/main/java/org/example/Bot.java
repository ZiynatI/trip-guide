package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Bot {
    String token;

    public Bot(String token) {
        this.token = token;
    }

    public void sendMessage(String message, String chatId) throws IOException {
        URL url = new URL("https://api.telegram.org/bot" + token + "/sendMessage?chat_id=" + chatId + "&parse_mode=MarkdownV2" + "&text=" + URLEncoder.encode(correctReservedChars(message), StandardCharsets.UTF_8));
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
        StringBuilder sb = new StringBuilder();
        List<Character> list = Arrays.asList('!', '*', '\'', '(', ')', ';', ':', '@', '&', '=', '+', '$', ',', '/', '?', '%', '#', '[', ']');
        for (int i = 0; i < message.length(); i++) {
            if (list.contains(message.charAt(i))) {
                sb.append("\\").append(message.charAt(i));
            } else {
                sb.append(message.charAt(i));
            }
        }
        return sb.toString();
    }
}
