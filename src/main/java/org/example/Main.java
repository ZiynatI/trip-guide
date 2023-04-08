package org.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        String[] tokenChatId = getUrlWithoutMessage();
        sendMessage("Hi)", tokenChatId[0], tokenChatId[1]);
    }

    public static String[] getUrlWithoutMessage() {
        Config cfg = ConfigFactory.parseFile(new File("application.conf"));
        return new String[]{cfg.getString("telegram.bot.token"), cfg.getString("telegram.chatId")};
    }

    public static void sendMessage(String message, String token, String chatId) throws IOException {
        URL url = new URL("https://api.telegram.org/bot" + token + "/sendMessage?chat_id=" + chatId + "&text=" + message);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        InputStream is = con.getResponseCode() <= 299 ? con.getInputStream() : con.getErrorStream();
        String content;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            content = reader.lines().collect(Collectors.joining("\n"));
        }
        System.out.println(content);
    }
}
