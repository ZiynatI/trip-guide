package org.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        sendMessage("Hi)");
    }

    public static String makeConfig() {
        Config cfg = ConfigFactory.parseFile(new File("application.conf"));
        return "https://api.telegram.org/bot" + cfg.getString("telegram.bot.token")
                + "/sendMessage?chat_id=" + cfg.getString("telegram.chatId") + "&text=";
    }

    public static void sendMessage(String s) throws IOException {
        URL url = new URL(makeConfig() + s);
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
