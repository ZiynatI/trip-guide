package org.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Config cfg = ConfigFactory.parseFile(new File("application.conf"));
        new Bot(cfg.getString("telegram.bot.token")).sendMessage("%D0%90%D0%91%D0%92%D0%93%D0%94%", cfg.getString("telegram.chatId"));
    }
}
